package org.easy4j.framework.core.jdbc.sql;

/**
 * @author: liuyong
 * @since 1.0
 */
public class InsertSQL extends AbstractSQL{


    public InsertSQL(String tableName) {
        super(tableName);
        sqlBuilder.append("INSERT INTO ")
                .append(tableName);
    }

}
