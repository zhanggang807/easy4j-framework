package org.easy4j.framework.core.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author: liuyong
 * @since 1.0
 */
public class BaseDao extends AbstractDao {

    @Autowired
    private DataSource dataSource ;

    @Override
    protected DataSource getDataSource() {
        return dataSource;
    }


    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public <T> T insert(String sql ,Class<?> type ,Object... params) throws Exception{
        Connection conn = this.prepareConnection();
        PreparedStatement stmt = null;
        try{
            stmt = this.prepareStatement(conn, sql,true);
            this.fillStatement(stmt, params);
            if( stmt.executeUpdate() <= 0 )
                throw new SQLException("insert.sql.error");
            return getGeneratedKey(stmt.getGeneratedKeys(),type);
        } catch (SQLException e) {
            this.rethrow(e, sql, params);
        }  finally {
            close(conn);
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
    public int update(String sql, Object... params) throws SQLException {
        Connection conn = this.prepareConnection();
        int rows = 0;
        try{
            rows = this.update(conn, sql, params);
        } catch (SQLException e) {
            this.rethrow(e, sql, params);
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
    private int update(Connection conn, String sql, Object... params) throws SQLException {


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
    public <T> T query(String sql, ResultSetHandler<T> rsh, Object... params) throws SQLException {
        return this.<T>query(sql, rsh, params);
    }

    /**
     * Execute an SQL SELECT query without any replacement parameters.  The
     * caller is responsible for closing the connection.
     * @param <T> The type of object that the handler returns
     * @param conn The connection to execute the query in.
     * @param sql The query to execute.
     * @param rsh The handler that converts the results into an object.
     * @return The object returned by the handler.
     * @throws SQLException if a database access error occurs
     */
    public <T> T query(Connection conn, String sql, ResultSetHandler<T> rsh) throws SQLException {
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
            throws SQLException {
        if (conn == null) {
            throw new SQLException("Null connection");
        }

        if (rsh == null) {
            throw new SQLException("Null ResultSetHandler");
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
            close(rs);
        }

        return result;
    }

    @Override
    protected Connection prepareConnection() throws SQLException {

        DataSource dataSource = getDataSource();

        if (dataSource == null) {
            throw new SQLException(
                    "Dao requires a DataSource to be "
                            + "invoked in this way, or a Connection should be passed in");
        }
        return DataSourceUtils.getConnection(dataSource);
    }

    protected void close(Connection conn) throws SQLException {
        DataSourceUtils.releaseConnection(conn, getDataSource());
    }


}
