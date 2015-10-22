package org.easy4j.framework.core.jdbc;


import org.easy4j.framework.core.jdbc.sql.*;

import java.util.LinkedHashMap;
import java.util.Map;

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



    public static String generateUpdateSQL(String tableName,String sets ,String conditions){
        AbstractSQL sql = new UpdateSQL(tableName);
        sql.append(sets);
        sql.appendConditions(conditions);
        return sql.build();
    }


    public static String generateInsertSQL(String tableName,String ... columns){
        InsertSQL sql = new InsertSQL(tableName);
        sql.insertColumns(columns);
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
        System.out.println(generateInsertSQL("user", "id", "name", "age"));
        Map<String,String> fieldMapping = new LinkedHashMap<String, String>(10);
        fieldMapping.put("id","id");
        fieldMapping.put("name","name");
        fieldMapping.put("age","age");

        System.out.println(generateInsertSQL("user", (String[])fieldMapping.values().toArray()));
    }
}
