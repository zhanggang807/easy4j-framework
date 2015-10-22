package org.easy4j.framework.core.jdbc.sql;

/**
 * @author: liuyong
 * @since 1.0
 */
public abstract class AbstractSQL {

    protected StringBuilder sqlBuilder  ;

    protected String tableName ;

    public AbstractSQL getSelf() {
        return this;
    }

    public AbstractSQL(String tableName){
        this.tableName  = tableName;
        sqlBuilder = new StringBuilder();
    }

    public AbstractSQL append(String sqlStr){
        sqlBuilder.append(sqlStr);
        return this ;
    }

    public  AbstractSQL appendColumns(String columns){
        //do-nothing
        return this ;
    }

    public AbstractSQL appendConditions(String conditions){

        if(conditions != null && !conditions.isEmpty()){
            sqlBuilder.append(" WHERE ").append(conditions);
        }

        return this;
    }


    public String build(){
        return sqlBuilder.toString();
    }
}