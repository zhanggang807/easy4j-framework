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



    protected  AbstractSQL appendColumns(String ... columns){
        return this;
    }

    protected String build(){
        return sqlBuilder.toString();
    }
}