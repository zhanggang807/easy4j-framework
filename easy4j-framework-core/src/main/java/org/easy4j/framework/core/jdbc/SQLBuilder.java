package org.easy4j.framework.core.jdbc;


import org.easy4j.framework.core.jdbc.sql.*;

/**
 * @author: liuyong
 * @since 1.0
 */
public class SQLBuilder {


    public static String generateSelectSQL(String columns ,String tableName ,String conditions){
        AbstractSQL sql = new SelectSQL(tableName);
        sql.appendColumns(columns);
        sql.appendConditions(conditions);
        return sql.build();
    }

    public static String generateSelectSqlForPager(String columns ,String tableName ,String conditions ,int pageNumber,int pageSize ){
        SelectSQL sql = new SelectSQL(tableName);
        sql.appendColumns(columns);
        sql.appendConditions(conditions);
        sql.appendSqlForPager(pageNumber,pageSize);
        return sql.build();
    }



    public static String generateUpdateSQL(String tableName,String conditions){
        AbstractSQL sql = new UpdateSQL(tableName);
        sql.appendConditions(conditions);
        return sql.build();
    }

    public static String generateInsertSQL(String tableName,String conditions){
        AbstractSQL sql = new InsertSQL(tableName);
        sql.appendConditions(conditions);
        return sql.build();
    }

    public static String generateDeleteSQL(String tableName,String conditions){
        AbstractSQL sql = new DeleteSQL(tableName);
        sql.appendConditions(conditions);
        return sql.build();
    }


    public static void main(String args[]){
        System.out.println(generateSelectSQL("","user","id=?"));
        System.out.println(generateSelectSQL(null,"user","id=?"));
        System.out.println(generateSelectSQL("id,name,age","user",""));
        System.out.println(generateSelectSQL("id,name,age", "user", null));
        System.out.println(generateDeleteSQL("user", null));
        System.out.println(generateDeleteSQL("user", "id=?"));
    }
}
