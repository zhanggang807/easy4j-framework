package org.easy4j.framework.core.jdbc;

import org.easy4j.framework.core.jdbc.filter.PropertyFilter;
import org.easy4j.framework.core.jdbc.handler.BeanHandler;
import org.easy4j.framework.core.jdbc.handler.BeanListHandler;
import org.easy4j.framework.core.jdbc.handler.SingleValueHandler;
import org.easy4j.framework.core.util.ReflectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: liuyong
 * @since 1.0
 */
public class BaseDao<M> extends AbstractDao {

    @Autowired(required = false)
    private DataSource dataSource ;

    protected final Class<M> beanClass;

    protected String tableName ;

    protected String INSERT = "$insert_";

    protected ResultSetHandler<M> resultSetHandler ;

    protected final Map<String ,String> sqlCache  ;

    protected static QueryRunner queryRunner = new QueryRunner();

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

    protected QueryRunner getQueryRunner(){
        return queryRunner;
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

    public M queryObject(String sql ,Object ... params){
        return this.query(sql ,resultSetHandler ,params);
    }

    public M findOne(String sql , Object... params) {
        return this.query(sql, resultSetHandler, params);
    }

    public List<M> queryList(String sql,Object... params) {
        return this.query(sql,new BeanListHandler<M>(beanClass),params);
    }

    public Object querySingleValue(String sql,Object... params) {
        return this.query(sql, new SingleValueHandler(), params);
    }


    protected <T> T insert(String sql ,Class<?> type ,Object... params) {

        Connection        conn =  null ;
        PreparedStatement stmt = null;
        try{
            conn = this.prepareConnection();
            queryRunner.insert(conn ,sql ,params);
            stmt = queryRunner.prepareStatement(conn, sql, Statement.RETURN_GENERATED_KEYS);
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

    @Override
    protected void close(Connection conn)  {
        DataSourceUtils.releaseConnection(conn, getDataSource());
    }


    //==============================private method=========================================================
    /**
     * Calls update after checking the parameters to ensure nothing is null.
     * @param sql The SQL statement to execute.
     * @param params An array of update replacement parameters.  Each row in
     * this array is one set of update replacement values.
     * @return The number of rows updated.
     * @throws SQLException If there are database or parameter errors.
     */
    private int update(String sql, Object... params) {

        Connection connection = null ;
        int rows = 0;

        try {
            connection = this.prepareConnection();
            rows = queryRunner.update(connection ,sql ,params);
        } catch (SQLException e) {
            this.rethrow(e, sql, params);
        } finally {
            close(connection);
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
    private  <T> T query(String sql, ResultSetHandler<T> rsh, Object... params)  {

        Connection connection = null;
        T result = null ;
        try {
            connection = this.prepareConnection();
            result = queryRunner.query(connection, sql, rsh, params);
        } catch (SQLException e) {
            rethrow(e, sql, params);
        } finally {
            close(connection);
        }

        return result ;
    }



}
