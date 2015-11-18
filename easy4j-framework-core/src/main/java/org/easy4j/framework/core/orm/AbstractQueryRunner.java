package org.easy4j.framework.core.orm;

import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.Arrays;

/**
 * The base class for QueryRunner & AsyncQueryRunner. This class is thread safe.
 *
 * @see org.easy4j.framework.core.orm.ResultSetHandler
 */
public  abstract class AbstractQueryRunner {


    /**
     * Is {@link java.sql.ParameterMetaData#getParameterType(int)} broken (have we tried
     * it yet)?
     */
    private volatile boolean pmdKnownBroken = false;

    public AbstractQueryRunner(){

    }

    /**
     * Constructor to control the use of <code>ParameterMetaData</code>.
     *
     * @param pmdKnownBroken
     *            Some drivers don't support
     *            {@link java.sql.ParameterMetaData#getParameterType(int) }; if
     *            <code>pmdKnownBroken</code> is set to true, we won't even try
     *            it; if false, we'll try it, and if it breaks, we'll remember
     *            not to use it again.
     */
    public AbstractQueryRunner(boolean pmdKnownBroken) {
        this.pmdKnownBroken = pmdKnownBroken;
    }

    /**
     * Some drivers don't support
     * {@link java.sql.ParameterMetaData#getParameterType(int) }; if
     * <code>pmdKnownBroken</code> is set to true, we won't even try it; if
     * false, we'll try it, and if it breaks, we'll remember not to use it
     * again.
     *
     * @return the flag to skip (or not)
     *         {@link java.sql.ParameterMetaData#getParameterType(int) }
     * @since 1.4
     */
    public boolean isPmdKnownBroken() {
        return pmdKnownBroken;
    }

    /**
     * Factory method that creates and initializes a
     * <code>PreparedStatement</code> object for the given SQL.
     * <code>QueryRunner</code> methods always call this method to prepare
     * statements for them. Subclasses can override this method to provide
     * special PreparedStatement configuration if needed. This implementation
     * simply calls <code>conn.prepareStatement(sql)</code>.
     *
     * @param conn
     *            The <code>Connection</code> used to create the
     *            <code>PreparedStatement</code>
     * @param sql
     *            The SQL statement to prepare.
     * @return An initialized <code>PreparedStatement</code>.
     * @throws java.sql.SQLException
     *             if a database access error occurs
     */
    protected PreparedStatement prepareStatement(Connection conn, String sql)
            throws SQLException {

        return conn.prepareStatement(sql);
    }

    /**
     * Factory method that creates and initializes a
     * <code>PreparedStatement</code> object for the given SQL.
     * <code>QueryRunner</code> methods always call this method to prepare
     * statements for them. Subclasses can override this method to provide
     * special PreparedStatement configuration if needed. This implementation
     * simply calls <code>conn.prepareStatement(sql, returnedKeys)</code>
     * which will result in the ability to retrieve the automatically-generated
     * keys from an auto_increment column.
     *
     * @param conn
     *            The <code>Connection</code> used to create the
     *            <code>PreparedStatement</code>
     * @param sql
     *            The SQL statement to prepare.
     * @param returnedKeys
     *            Flag indicating whether to return generated keys or not.
     *
     * @return An initialized <code>PreparedStatement</code>.
     * @throws SQLException
     *             if a database access error occurs
     * @since 1.6
     */
    protected PreparedStatement prepareStatement(Connection conn, String sql, int returnedKeys)
            throws SQLException {

        return conn.prepareStatement(sql, returnedKeys);
    }

    /**
     * Factory method that creates and initializes a <code>Connection</code>
     * object. <code>QueryRunner</code> methods always call this method to
     * retrieve connections from its DataSource. Subclasses can override this
     * method to provide special <code>Connection</code> configuration if
     * needed. This implementation simply calls <code>ds.getConnection()</code>.
     *
     * @return An initialized <code>Connection</code>.
     * @throws SQLException
     *             if a database access error occurs
     * @since DbUtils 1.1
     */
    protected abstract Connection prepareConnection() throws SQLException ;

    /**
     * Returns the <code>DataSource</code> this runner is using.
     * <code>QueryRunner</code> methods always call this method to get the
     * <code>DataSource</code> so subclasses can provide specialized behavior.
     *
     * @return DataSource the runner is using
     */
    protected abstract DataSource getDataSource() ;

    /**
     * Fill the <code>PreparedStatement</code> replacement parameters with the
     * given objects.
     *
     * @param stmt
     *            PreparedStatement to fill
     * @param params
     *            Query replacement parameters; <code>null</code> is a valid
     *            value to pass in.
     * @throws SQLException
     *             if a database access error occurs
     */
    public void fillStatement(PreparedStatement stmt, Object... params)
            throws SQLException {

        // check the parameter count, if we can
        ParameterMetaData pmd = null;
        if (!pmdKnownBroken) {
            pmd = stmt.getParameterMetaData();
            int stmtCount = pmd.getParameterCount();
            int paramsCount = params == null ? 0 : params.length;

            if (stmtCount != paramsCount) {
                throw new SQLException("Wrong number of parameters: expected "
                        + stmtCount + ", was given " + paramsCount);
            }
        }

        // nothing to do here
        if (params == null) {
            return;
        }

        for (int i = 0; i < params.length; i++) {
            if (params[i] != null) {
                stmt.setObject(i + 1, params[i]);
            } else {
                // VARCHAR works with many drivers regardless
                // of the actual column type. Oddly, NULL and
                // OTHER don't work with Oracle's drivers.
                int sqlType = Types.VARCHAR;
                if (!pmdKnownBroken) {
                    try {
                        /*
                         * It's not possible for pmdKnownBroken to change from
                         * true to false, (once true, always true) so pmd cannot
                         * be null here.
                         */
                        sqlType = pmd.getParameterType(i + 1);
                    } catch (SQLException e) {
                        pmdKnownBroken = true;
                    }
                }
                stmt.setNull(i + 1, sqlType);
            }
        }
    }

    /**
     * Fill the <code>PreparedStatement</code> replacement parameters with the
     * given object's bean property values.
     *
     * @param stmt
     *            PreparedStatement to fill
     * @param bean
     *            a JavaBean object
     * @param properties
     *            an ordered array of properties; this gives the order to insert
     *            values in the statement
     * @throws SQLException
     *             if a database access error occurs
     */
    public void fillStatementWithBean(PreparedStatement stmt, Object bean,
                                      PropertyDescriptor[] properties) throws SQLException {
        Object[] params = new Object[properties.length];
        for (int i = 0; i < properties.length; i++) {
            PropertyDescriptor property = properties[i];
            Object value = null;
            Method method = property.getReadMethod();
            if (method == null) {
                throw new RuntimeException("No read method for bean property "
                        + bean.getClass() + " " + property.getName());
            }
            try {
                value = method.invoke(bean, new Object[0]);
            } catch (InvocationTargetException e) {
                throw new RuntimeException("Couldn't invoke method: " + method,
                        e);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException(
                        "Couldn't invoke method with 0 arguments: " + method, e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Couldn't invoke method: " + method,
                        e);
            }
            params[i] = value;
        }
        fillStatement(stmt, params);
    }

    /**
     * Fill the <code>PreparedStatement</code> replacement parameters with the
     * given object's bean property values.
     *
     * @param stmt
     *            PreparedStatement to fill
     * @param bean
     *            A JavaBean object
     * @param propertyNames
     *            An ordered array of property names (these should match the
     *            getters/setters); this gives the order to insert values in the
     *            statement
     * @throws SQLException
     *             If a database access error occurs
     */
    public void fillStatementWithBean(PreparedStatement stmt, Object bean,
                                      String... propertyNames) throws SQLException {
        PropertyDescriptor[] descriptors;
        try {
            descriptors = Introspector.getBeanInfo(bean.getClass())
                    .getPropertyDescriptors();
        } catch (IntrospectionException e) {
            throw new RuntimeException("Couldn't introspect bean "
                    + bean.getClass().toString(), e);
        }
        PropertyDescriptor[] sorted = new PropertyDescriptor[propertyNames.length];
        for (int i = 0; i < propertyNames.length; i++) {
            String propertyName = propertyNames[i];
            if (propertyName == null) {
                throw new NullPointerException("propertyName can't be null: "
                        + i);
            }
            boolean found = false;
            for (int j = 0; j < descriptors.length; j++) {
                PropertyDescriptor descriptor = descriptors[j];
                if (propertyName.equals(descriptor.getName())) {
                    sorted[i] = descriptor;
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw new RuntimeException("Couldn't find bean property: "
                        + bean.getClass() + " " + propertyName);
            }
        }
        fillStatementWithBean(stmt, bean, sorted);
    }

    /**
     * Throws a new exception with a more informative error message.
     *
     * @param cause
     *            The original exception that will be chained to the new
     *            exception when it's rethrown.
     *
     * @param sql
     *            The query that was executing when the exception happened.
     *
     * @param params
     *            The query replacement parameters; <code>null</code> is a valid
     *            value to pass in.
     *
     * @throws DbAccessException
     *             if a database access error occurs
     */
    protected void rethrow(SQLException cause, String sql, Object... params) {

        String causeMessage = cause.getMessage();
        if (causeMessage == null) {
            causeMessage = "";
        }
        StringBuffer msg = new StringBuffer(causeMessage);

        msg.append(" Query: ");
        msg.append(sql);
        msg.append(" Parameters: ");

        if (params == null) {
            msg.append("[]");
        } else {
            msg.append(Arrays.deepToString(params));
        }

        DbAccessException e = new DbAccessException(msg.toString(), cause.getSQLState(),
                cause);

        throw e;
    }

    /**
     * Wrap the <code>ResultSet</code> in a decorator before processing it. This
     * implementation returns the <code>ResultSet</code> it is given without any
     * decoration.
     *
     * <p>
     * Often, the implementation of this method can be done in an anonymous
     * inner class like this:
     * </p>
     *
     * <pre>
     * QueryRunner run = new QueryRunner() {
     *     protected ResultSet wrap(ResultSet rs) {
     *         return StringTrimmedResultSet.wrap(rs);
     *     }
     * };
     * </pre>
     *
     * @param rs
     *            The <code>ResultSet</code> to decorate; never
     *            <code>null</code>.
     * @return The <code>ResultSet</code> wrapped in some decorator.
     */
    protected ResultSet wrap(ResultSet rs) {
        return rs;
    }

    /**
     * Close a <code>Connection</code>. This implementation avoids closing if
     * null and does <strong>not</strong> suppress any exceptions. Subclasses
     * can override to provide special handling like logging.
     *
     * @param conn
     *            Connection to close
     * @throws DbAccessException
     *             if a database access error occurs
     * @since DbUtils 1.1
     */
    protected void close(Connection conn) throws DbAccessException {
        DataSourceUtils.releaseConnection(conn, getDataSource());
    }

    /**
     * Close a <code>Statement</code>. This implementation avoids closing if
     * null and does <strong>not</strong> suppress any exceptions. Subclasses
     * can override to provide special handling like logging.
     *
     * @param stmt
     *            Statement to close
     * @throws DbAccessException
     *             if a database access error occurs
     * @since DbUtils 1.1
     */
    protected void close(Statement stmt) throws SQLException{
        DbUtils.close(stmt);
    }

    /**
     * Close a <code>Statement</code>. This implementation avoids closing if
     * null and does <strong>not</strong> suppress any exceptions. Subclasses
     * can override to provide special handling like logging.
     *
     * @param stmt
     *            Statement to close
     * @throws DbAccessException
     *             if a database access error occurs
     * @since DbUtils 1.1
     */
    protected void closeQuietly(Statement stmt) {
        try {
            DbUtils.close(stmt);
        } catch (SQLException e) {
            //quiet
        }
    }

    /**
     * Close a <code>ResultSet</code>. This implementation avoids closing if
     * null and does <strong>not</strong> suppress any exceptions. Subclasses
     * can override to provide special handling like logging.
     *
     * @param rs
     *            ResultSet to close
     * @throws DbAccessException
     *             if a database access error occurs
     * @since DbUtils 1.1
     */
    protected void close(ResultSet rs) throws DbAccessException {
        DbUtils.close(rs);
    }

    /**
     * Close a <code>ResultSet</code>. This implementation avoids closing if
     * null and does <strong>not</strong> suppress any exceptions. Subclasses
     * can override to provide special handling like logging.
     *
     * @param rs
     *            ResultSet to close
     * @throws DbAccessException
     *             if a database access error occurs
     * @since DbUtils 1.1
     */
    protected void closeQuietly(ResultSet rs) throws DbAccessException {
        DbUtils.closeQuietly(rs);
    }

    protected void close(ResultSet rs  ,Statement stmt, Connection connection){

    }

    protected void close(Statement stmt, Connection conn){
        try{
            DbUtils.close(stmt);
        } catch (SQLException s) {

        } finally {
            close(conn);
        }
    }

}
