package org.easy4j.framework.core.jdbc;

import javax.sql.DataSource;
import java.sql.*;

/**
 * @author bjliuyong
 * @version 1.0
 * @created date 15-10-19
 */
public class QueryRunner  extends AbstractQueryRunner{


    public QueryRunner(){

    }

    /**
     * Constructor to control the use of <code>ParameterMetaData</code>.
     *
     * @param pmdKnownBroken Some drivers don't support
     *                       {@link java.sql.ParameterMetaData#getParameterType(int) }; if
     *                       <code>pmdKnownBroken</code> is set to true, we won't even try
     *                       it; if false, we'll try it, and if it breaks, we'll remember
     *                       not to use it again.
     */
    public QueryRunner(boolean pmdKnownBroken) {
        super(pmdKnownBroken);
    }

    /**
     * Returns the <code>DataSource</code> this runner is using.
     * <code>QueryRunner</code> methods always call this method to get the
     * <code>DataSource</code> so subclasses can provide specialized behavior.
     *
     * @return DataSource the runner is using
     */
    @Override
    protected DataSource getDataSource() {
        return null;
    }

    /**
     * Execute an SQL SELECT query with replacement parameters.  The
     * caller is responsible for closing the connection.
     * @param <T> The type of object that the handler returns
     * @param conn The connection to execute the query in.
     * @param sql The query to execute.
     * @param rsh The handler that converts the results into an object.
     * @param params The replacement parameters.
     * @return The object returned by the handler.
     * @throws java.sql.SQLException if a database access error occurs
     */
    public <T> T query(Connection conn, String sql, ResultSetHandler<T> rsh, Object... params) throws SQLException {
        return this.<T>query(conn, false, sql, rsh, params);
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
        return this.<T>query(conn, false, sql, rsh, (Object[]) null);
    }

    /**
     * Executes the given INSERT, UPDATE, or DELETE SQL statement without
     * any replacement parameters. The <code>Connection</code> is retrieved
     * from the <code>DataSource</code> set in the constructor.  This
     * <code>Connection</code> must be in auto-commit mode or the update will
     * not be saved.
     *
     * @param sql The SQL statement to execute.
     * @throws SQLException if a database access error occurs
     * @return The number of rows updated.
     */
    public int update(Connection conn ,String sql) throws SQLException {
        return this.update(conn, true, sql, (Object[]) null);
    }

    /**
     * Executes the given INSERT, UPDATE, or DELETE SQL statement with
     * a single replacement parameter.  The <code>Connection</code> is
     * retrieved from the <code>DataSource</code> set in the constructor.
     * This <code>Connection</code> must be in auto-commit mode or the
     * update will not be saved.
     *
     * @param sql The SQL statement to execute.
     * @param param The replacement parameter.
     * @throws SQLException if a database access error occurs
     * @return The number of rows updated.
     */
    public int update(Connection conn  ,String sql, Object param) throws SQLException {
        return this.update(conn, true, sql, new Object[]{param});
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
    public int update(Connection conn ,String sql, Object... params) throws SQLException {
        return this.update(conn, true, sql, params);
    }


    /**
     * Executes the given INSERT SQL statement. The
     * <code>Connection</code> is retrieved from the <code>DataSource</code>
     * set in the constructor.  This <code>Connection</code> must be in
     * auto-commit mode or the insert will not be saved.
     * @param <T> The type of object that the handler returns
     * @param sql The SQL statement to execute.
     * @param rsh The handler used to create the result object from
     * the <code>ResultSet</code> of auto-generated keys.
     * @return An object generated by the handler.
     * @throws SQLException if a database access error occurs
     * @since 1.6
     */
    public <T> T insert(Connection conn ,String sql, ResultSetHandler<T> rsh) throws SQLException {
        return insert(conn, sql, rsh, (Object[]) null);
    }


    /**
     * Executes the given INSERT SQL statement. The
     * <code>Connection</code> is retrieved from the <code>DataSource</code>
     * set in the constructor.  This <code>Connection</code> must be in
     * auto-commit mode or the insert will not be saved.
     * @param <T> The type of object that the handler returns
     * @param sql The SQL statement to execute.
     * @param rsh The handler used to create the result object from
     * the <code>ResultSet</code> of auto-generated keys.
     * @param params Initializes the PreparedStatement's IN (i.e. '?')
     * @return An object generated by the handler.
     * @throws SQLException if a database access error occurs
     * @since 1.6
     */
    public <T> T insert(Connection conn ,String sql, ResultSetHandler<T> rsh, Object... params) throws SQLException {
        return insert(conn, false, sql, rsh, params);
    }


    /**
     * Execute an SQL INSERT query without replacement parameters.
     * @param conn The connection to use to run the query.
     * @param sql The SQL to execute.
     * the <code>ResultSet</code> of auto-generated keys.
     * @return An object generated by the handler.
     * @throws SQLException if a database access error occurs
     * @since 1.6
     */
    public int insert(Connection conn, String sql) throws SQLException {
        return insert(conn, sql, (Object[]) null);
    }


    /**
     * Execute an SQL INSERT query without replacement parameters.
     * @param conn The connection to use to run the query.
     * @param sql The SQL to execute.
     * the <code>ResultSet</code> of auto-generated keys.
     * @return An object generated by the handler.
     * @throws SQLException if a database access error occurs
     * @since 1.6
     */
    public int insert(Connection conn, String sql ,Object... params) throws SQLException {
        return insert(conn, false, sql, params);
    }



    /**
     * Calls query after checking the parameters to ensure nothing is null.
     * @param conn The connection to use for the query call.
     * @param closeConn True if the connection should be closed, false otherwise.
     * @param sql The SQL statement to execute.
     * @param params An array of query replacement parameters.  Each row in
     * this array is one set of batch replacement values.
     * @return The results of the query.
     * @throws DbAccessException If there are database or parameter errors.
     */
    public  <T> T query(Connection conn, boolean closeConn, String sql, ResultSetHandler<T> rsh, Object... params) throws SQLException{

        PreparedStatement stmt = null;
        ResultSet rs = null;
        T result = null;

        try {
            stmt = this.prepareStatement(conn, sql);
            this.fillStatement(stmt, params);
            rs = this.wrap(stmt.executeQuery());
            result = rsh.handle(rs);

        } finally {
            closeQuietly(rs);
            closeQuietly(stmt);
            if (closeConn)
                close(conn);

        }

        return result;
    }

    /**
     * Calls update after checking the parameters to ensure nothing is null.
     * @param conn The connection to use for the update call.
     * @param closeConn True if the connection should be closed, false otherwise.
     * @param sql The SQL statement to execute.
     * @param params An array of update replacement parameters.  Each row in
     * this array is one set of update replacement values.
     * @return The number of rows updated.
     * @throws SQLException If there are database or parameter errors.
     */
    public int update(Connection conn, boolean closeConn, String sql, Object... params) throws SQLException {

        PreparedStatement stmt = null;
        int rows = 0;

        try {
            stmt = this.prepareStatement(conn, sql);
            this.fillStatement(stmt, params);
            rows = stmt.executeUpdate();

        } catch (SQLException e) {
            this.rethrow(e, sql, params);

        } finally {
            close(stmt);
            if (closeConn) {
                close(conn);
            }
        }

        return rows;
    }


    /**
     * Executes the given INSERT SQL statement.
     * @param conn The connection to use for the query call.
     * @param closeConn True if the connection should be closed, false otherwise.
     * @param sql The SQL statement to execute.
     * @param rsh The handler used to create the result object from
     * the <code>ResultSet</code> of auto-generated keys.
     * @param params The query replacement parameters.
     * @return An object generated by the handler.
     * @throws SQLException If there are database or parameter errors.
     * @since 1.6
     */
    public <T> T insert(Connection conn, boolean closeConn, String sql, ResultSetHandler<T> rsh, Object... params)
            throws SQLException {

        PreparedStatement stmt = null;
        T generatedKeys = null;
        ResultSet resultSet = null ;

        try {
            stmt = this.prepareStatement(conn ,sql, Statement.RETURN_GENERATED_KEYS);
            this.fillStatement(stmt, params);
            stmt.executeUpdate();
            resultSet = stmt.getGeneratedKeys();
            generatedKeys = rsh.handle(resultSet);


        } catch (SQLException e) {
            this.rethrow(e, sql, params);
        } finally {
            closeQuietly(resultSet);
            closeQuietly(stmt);
            if (closeConn) {
                close(conn);
            }
        }

        return generatedKeys;
    }


    /**
     * Executes the given INSERT SQL statement.
     * @param conn The connection to use for the query call.
     * @param closeConn True if the connection should be closed, false otherwise.
     * @param sql The SQL statement to execute.
     * the <code>ResultSet</code> of auto-generated keys.
     * @param params The query replacement parameters.
     * @return .
     * @throws SQLException If there are database or parameter errors.
     * @since 1.6
     */
    public int insert(Connection conn, boolean closeConn, String sql,  Object... params)
            throws SQLException {

        PreparedStatement stmt = null;
        ResultSet resultSet = null ;
        int rows = 0 ;
        try {
            stmt = this.prepareStatement(conn ,sql) ;
            this.fillStatement(stmt, params);
            rows = stmt.executeUpdate();

        } catch (SQLException e) {
            this.rethrow(e, sql, params);
        } finally {
            closeQuietly(resultSet);
            closeQuietly(stmt);
            if (closeConn) {
                close(conn);
            }
        }

        return rows;
    }
}
