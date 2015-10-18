package org.easy4j.framework.core.jdbc;

import org.easy4j.framework.core.jdbc.filter.PropertyFilter;
import org.easy4j.framework.core.jdbc.handler.BeanHandler;
import org.easy4j.framework.core.jdbc.handler.BeanListHandler;
import org.easy4j.framework.core.jdbc.handler.SingleValueHandler;
import org.easy4j.framework.core.util.ReflectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: liuyong
 * @since 1.0
 */
public class BaseDao<M> extends AbstractDao {

    @Autowired
    private DataSource dataSource ;

    protected final Class<M> beanClass;

    protected String tableName ;

    protected String INSERT = "$insert_";

    protected ResultSetHandler<M> resultSetHandler ;

    protected final Map<String ,String> sqlCache  ;

    public BaseDao(){
        this.sqlCache  = new HashMap<String, String>();
        this.beanClass = ReflectUtils.findParameterizedType(getClass(), 0);
        if(beanClass != null ) {
            this.tableName = JdbcUtils.tableName(this.beanClass);
            //Map columnMap = new HashMap();
            resultSetHandler = new BeanHandler<M>(beanClass ) ;
            //resultSetHandler = new BeanHandler<M>(beanClass,new BasicRowProcessor(new BeanProcessor(columnMap) )) ;
        }


        _initSql();
        initSql();
    }

    protected void initSql(){

    }

    /**
     * 初始化部分SQL
     */
    protected void _initSql(){

        String[] columns = JdbcUtils.columns(this.beanClass);

        SQL insertSQL = new SQL().INSERT_INTO(tableName);
        for(String column : columns){
            if(column.equals("id"))
                continue;
            insertSQL.VALUES(column, "?");
        }

        cacheSql(INSERT , insertSQL.toString());

    };

    protected String sql(String key ){
        return sqlCache.get(key);
    }

    protected String cacheSql(String key ,String value){
        return sqlCache.put(key,value);
    }

    @Override
    protected DataSource getDataSource() {
        return dataSource;
    }


    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * 增加一条记录 ，并返回影响的行数
     * @param m
     * @return
     */
    public int save(M m) {

        Object[] parameter = JdbcUtils.values(m, PropertyFilter.ID_FILTER);

        String insertSql = sql(INSERT);
        return update(insertSql,parameter);
    }

    public M findOne(String sql , Object... params) {
        return this.query(sql ,resultSetHandler ,params);
    }

    public List<M> queryList(String sql,Object... params) {
        return this.query(sql,new BeanListHandler<M>(beanClass),params);
    }

    public Object querySingleValue(String sql,Object... params) {
        return this.query(sql,new SingleValueHandler(),params);
    }


    protected <T> T insert(String sql ,Class<?> type ,Object... params) {
        Connection        conn =  null ;
        PreparedStatement stmt = null;
        try{
            conn = this.prepareConnection();
            stmt = this.prepareStatement(conn, sql,true);
            this.fillStatement(stmt, params);
            if( stmt.executeUpdate() <= 0 )
                throw new SQLException("insert.sql.error");
            return getGeneratedKey(stmt.getGeneratedKeys(),type);
        } catch (SQLException e) {
            this.rethrow(e, sql, params);
        }  finally {
            try{
                close(stmt);
            } finally {
                close(conn);
            }
        }
        return null;
    }

    /**
     * Executes the given INSERT, UPDATE, or DELETE SQL statement.  The
     * <code>Connection</code> is retrieved from the <code>DataSource</code>
     * set in the constructor.  This <code>Connection</code> must be in
     * auto-commit mode or the update will not be saved.
     *
     * @param sql The SQL statement to execute.
     * @param params Initializes the PreparedStatement's IN (i.e. '?')
     * parameters.
     * @throws SQLException if a database access error occurs
     * @return The number of rows updated.
     */
    public int update(String sql, Object... params) throws DbAccessException {
        Connection conn = null;
        int rows = 0;
        try{
            conn = this.prepareConnection();
            rows = this.update(conn, sql, params);
        }  finally {
            close(conn);
        }
        return rows;
    }

    /**
     * Calls update after checking the parameters to ensure nothing is null.
     * @param conn The connection to use for the update call.
     * @param sql The SQL statement to execute.
     * @param params An array of update replacement parameters.  Each row in
     * this array is one set of update replacement values.
     * @return The number of rows updated.
     * @throws SQLException If there are database or parameter errors.
     */
    private int update(Connection conn, String sql, Object... params) {


        PreparedStatement stmt = null;
        int rows = 0;

        try {
            stmt = this.prepareStatement(conn, sql,false);
            this.fillStatement(stmt, params);
            rows = stmt.executeUpdate();

        } catch (SQLException e) {
            this.rethrow(e, sql, params);
        } finally {
            close(stmt);
        }

        return rows;
    }

    /**
     * Execute an SQL SELECT query with replacement parameters.  The
     * caller is responsible for closing the connection.
     * @param <T> The type of object that the handler returns
     * @param sql The query to execute.
     * @param rsh The handler that converts the results into an object.
     * @param params The replacement parameters.
     * @return The object returned by the handler.
     * @throws SQLException if a database access error occurs
     */
    public <T> T query(String sql, ResultSetHandler<T> rsh, Object... params)  {
        Connection connection = null;
        try{
            connection = this.prepareConnection();
            return this.<T>query(connection ,sql, rsh, params);
        }finally {
            close(connection);
        }
    }

    /**
     * Execute an SQL SELECT query without any replacement parameters.  The
     * caller is responsible for closing the connection.
     * @param <T> The type of object that the handler returns
     * @param sql The query to execute.
     * @param rsh The handler that converts the results into an object.
     * @return The object returned by the handler.
     * @throws SQLException if a database access error occurs
     */
    public <T> T query(String sql, ResultSetHandler<T> rsh)  {
        return this.<T>query(sql, rsh, (Object[]) null);
    }

    /**
     * Calls query after checking the parameters to ensure nothing is null.
     * @param conn The connection to use for the query call.
     * @param sql The SQL statement to execute.
     * @param params An array of query replacement parameters.  Each row in
     * this array is one set of batch replacement values.
     * @return The results of the query.
     * @throws SQLException If there are database or parameter errors.
     */
    private <T> T query(Connection conn,  String sql, ResultSetHandler<T> rsh, Object... params)
             {
        if (conn == null) {
            throw new DbAccessException("Null connection");
        }

        if (rsh == null) {
            throw new DbAccessException("Null ResultSetHandler");
        }

        PreparedStatement stmt = null;
        ResultSet rs = null;
        T result = null;

        try {
            stmt = this.prepareStatement(conn, sql,false);
            this.fillStatement(stmt, params);
            rs = this.wrap(stmt.executeQuery());
            result = rsh.handle(rs);

        } catch (SQLException e) {
            this.rethrow(e, sql, params);
        } finally {
            try {
                close(rs);
            } finally {
                close(stmt);
            }
        }

        return result;
    }

    @Override
    protected Connection prepareConnection()  {

        DataSource dataSource = getDataSource();

        if (dataSource == null) {
            throw new DbAccessException(
                    "Dao requires a DataSource to be "
                            + "invoked in this way, or a Connection should be passed in");
        }
        return DataSourceUtils.getConnection(dataSource);
    }

    protected void close(Connection conn)  {
        DataSourceUtils.releaseConnection(conn, getDataSource());
    }


}
