package org.easy4j.framework.core.jdbc.sql;

/**
 * @author: liuyong
 * @since 1.0
 */
public class SelectSQL extends AbstractSQL {

    private static final String SELECT_ALL_COLUMNS = "SELECT * FROM " ;

    public SelectSQL(String tableName) {
        super(tableName);
    }

    public  AbstractSQL appendColumns(String columns){

        if(columns == null || columns.isEmpty()){
            sqlBuilder.append(SELECT_ALL_COLUMNS).append(tableName);
        } else {
            sqlBuilder
                    .append("SELECT ")
                    .append(columns)
                    .append(" FROM ")
                    .append(tableName);
        }
        return this ;
    }

    public AbstractSQL appendSqlForPager(int pageNumber,int pageSize){

        int start = 0 ;

        if(pageNumber > 1)
            start = (pageNumber - 1) * pageSize ;

        sqlBuilder.append(" LIMIT ").append(start).append(",").append(pageSize);
        return this ;
    }
}
