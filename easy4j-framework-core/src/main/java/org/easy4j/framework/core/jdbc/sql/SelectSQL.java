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
            sqlBuilder.append(SELECT_ALL_COLUMNS);
        } else {
            sqlBuilder
                    .append("SELECT ")
                    .append(columns)
                    .append(" FROM ")
                    .append(tableName);
        }
        return this ;
    }

    public AbstractSQL appendSqlForPager(int pageNumber, int pageSize ){

        int pageStart = 0;

        if(pageNumber > 1 )
            pageStart = (pageNumber - 1) * pageSize;

        sqlBuilder.append(" LIMIT ").append(pageStart).append(",").append(pageSize);
        return this ;
    }
}