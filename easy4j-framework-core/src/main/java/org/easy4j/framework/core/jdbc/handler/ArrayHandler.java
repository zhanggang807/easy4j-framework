package org.easy4j.framework.core.jdbc.handler;

/**
 * @author: liuyong
 * @since 1.0
 */

import org.easy4j.framework.core.jdbc.BasicRowProcessor;
import org.easy4j.framework.core.jdbc.ResultSetHandler;
import org.easy4j.framework.core.jdbc.RowProcessor;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <code>ResultSetHandler</code> implementation that converts a
 * <code>ResultSet</code> into an <code>Object[]</code>. This class is
 * thread safe.
 *
 * @see org.easy4j.framework.core.jdbc.ResultSetHandler
 */
public class ArrayHandler implements ResultSetHandler<Object[]> {

    /**
     * Singleton processor instance that handlers share to save memory.  Notice
     * the default scoping to allow only classes in this package to use this
     * instance.
     */
    static final RowProcessor ROW_PROCESSOR = new BasicRowProcessor();

    /**
     * The RowProcessor implementation to use when converting rows
     * into arrays.
     */
    private final RowProcessor convert;

    /**
     * Creates a new instance of ArrayHandler using a
     * <code>BasicRowProcessor</code> for conversion.
     */
    public ArrayHandler() {
        this(ROW_PROCESSOR);
    }

    /**
     * Creates a new instance of ArrayHandler.
     *
     * @param convert The <code>RowProcessor</code> implementation
     * to use when converting rows into arrays.
     */
    public ArrayHandler(RowProcessor convert) {
        super();
        this.convert = convert;
    }

    /**
     * Places the column values from the first row in an <code>Object[]</code>.
     * @param rs <code>ResultSet</code> to process.
     * @return An Object[] or <code>null</code> if there are no rows in the
     * <code>ResultSet</code>.
     *
     * @throws java.sql.SQLException if a database access error occurs
     * @see org.easy4j.framework.core.jdbc.ResultSetHandler#handle(java.sql.ResultSet)
     */
    public Object[] handle(ResultSet rs) throws SQLException {
        return rs.next() ? this.convert.toArray(rs) : null;
    }

}
