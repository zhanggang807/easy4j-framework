package org.easy4j.framework.core.jdbc.handler;

import org.easy4j.framework.core.jdbc.ResultSetHandler;
import org.easy4j.framework.core.jdbc.RowProcessor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * <code>ResultSetHandler</code> implementation that converts a
 * <code>ResultSet</code> into a <code>List</code> of beans. This class is
 * thread safe.
 *
 * @param <T> the target bean type
 * @see ResultSetHandler
 */
public class BeanListHandler<T> implements ResultSetHandler<List<T>> {

    /**
     * The Class of beans produced by this handler.
     */
    private final Class<T> type;

    /**
     * The RowProcessor implementation to use when converting rows
     * into beans.
     */
    private final RowProcessor convert;

    /**
     * Creates a new instance of BeanListHandler.
     *
     * @param type The Class that objects returned from <code>handle()</code>
     * are created from.
     */
    public BeanListHandler(Class<T> type) {
        this(type, ArrayHandler.ROW_PROCESSOR);
    }

    /**
     * Creates a new instance of BeanListHandler.
     *
     * @param type The Class that objects returned from <code>handle()</code>
     * are created from.
     * @param convert The <code>RowProcessor</code> implementation
     * to use when converting rows into beans.
     */
    public BeanListHandler(Class<T> type, RowProcessor convert) {
        this.type = type;
        this.convert = convert;
    }

    /**
     * Convert the whole <code>ResultSet</code> into a List of beans with
     * the <code>Class</code> given in the constructor.
     *
     * @param rs The <code>ResultSet</code> to handle.
     *
     * @return A List of beans, never <code>null</code>.
     *
     * @throws java.sql.SQLException if a database access error occurs
     * @see RowProcessor#toBeanList(java.sql.ResultSet, Class)
     */
    public List<T> handle(ResultSet rs) throws SQLException {
        return this.convert.toBeanList(rs, type);
    }
}
