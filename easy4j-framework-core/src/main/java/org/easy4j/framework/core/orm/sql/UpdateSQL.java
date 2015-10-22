package org.easy4j.framework.core.orm.sql;

/**
 * @author: liuyong
 * @since 1.0
 */
public class UpdateSQL extends AbstractSQL {


    public UpdateSQL(String tableName) {
        super(tableName);
        sqlBuilder.append("UPDATE ")
                .append(tableName)
                .append(" SET ");
    }
}
