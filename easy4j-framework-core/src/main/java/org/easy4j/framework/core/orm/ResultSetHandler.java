package org.easy4j.framework.core.orm;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Implementations of this interface convert ResultSets into other objects.
 *
 * @param <T> the target type the input ResultSet will be converted to.
 *
 * @author: liuyong
 * @since 1.0
 */
public interface ResultSetHandler<T> {


    /**
     * Turn the <code>ResultSet</code> into an Object.
     *
     * @param rs The <code>ResultSet</code> to handle.  It has not been touched
     * before being passed to this method.
     *
     * @return An Object initialized with <code>ResultSet</code> data. It is
     * legal for implementations to return <code>null</code> if the
     * <code>ResultSet</code> contained 0 rows.
     *
     * @throws java.sql.SQLException if a database access error occurs
     */
    T handle(ResultSet rs) throws SQLException;

}
