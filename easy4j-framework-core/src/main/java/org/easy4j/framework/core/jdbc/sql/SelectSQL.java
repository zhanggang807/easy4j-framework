package org.easy4j.framework.core.jdbc.sql;

/**
 * @author: liuyong
 * @since 1.0
 */
public class SelectSQL extends AbstractSQL {


    public SelectSQL(String tableName) {
        super(tableName);
        sqlBuilder.append("SELECT ");
    }
}
