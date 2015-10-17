package org.easy4j.framework.core.jdbc.handler;

import org.easy4j.framework.core.jdbc.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author: liuyong
 * @since 1.0
 */
public class SingleValueHandler implements ResultSetHandler {

    @Override
    public String handle(ResultSet rs) throws SQLException {

        if (rs.next()) {
            return rs.getString(1);

        } else {
            return null;
        }

    }
}
