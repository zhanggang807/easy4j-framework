package org.easy4j.framework.core.orm.sql;

/**
 * @author: liuyong
 * @since 1.0
 */
public class InsertSQL extends AbstractSQL{


    public InsertSQL(String tableName) {
        super(tableName);
        sqlBuilder.append("INSERT INTO ")
                .append(tableName)
                .append('(') ;
    }

    public String insertColumns(String[] columns){

        StringBuilder placeHolder = new StringBuilder();

        for(String column : columns){
            sqlBuilder.append(column).append(',');
            placeHolder.append('?').append(',') ;
        }

        sqlBuilder.deleteCharAt(sqlBuilder.length() - 1);
        placeHolder.deleteCharAt(placeHolder.length() - 1);

        sqlBuilder.append(") VALUES(");
        placeHolder.append(')');

        sqlBuilder.append(placeHolder);

        return sqlBuilder.toString() ;
    }

}
