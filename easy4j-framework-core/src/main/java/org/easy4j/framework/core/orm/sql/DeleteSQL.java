package org.easy4j.framework.core.orm.sql;

/**
 * @author: liuyong
 * @since 1.0
 */
public class DeleteSQL extends AbstractSQL {


    public DeleteSQL(String tableName) {
        super(tableName);
        sqlBuilder.append("DELETE FROM ")
                .append(tableName);
    }
}
