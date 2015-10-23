package org.easy4j.framework.core.orm;


import org.easy4j.framework.core.orm.sql.*;

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
        StringBuilder sql = new StringBuilder();
             sql.append("UPDATE ")
                .append(tableName)
                .append(" SET ")
                .append(sets);
        appendConditions(sql,conditions);
        return sql.toString();
    }

    public static String generateInsertSQL(String tableName,Object ... columns){
        StringBuilder sql = new StringBuilder("INSERT INTO ");
        sql.append(tableName).append('(');
        StringBuilder placeHolder = new StringBuilder();
        for(Object column : columns){
            sql.append(column).append(',');
            placeHolder.append('?').append(',') ;
        }

        sql.deleteCharAt(sql.length() - 1);
        placeHolder.deleteCharAt(placeHolder.length() - 1);

        sql.append(") VALUES(");
        placeHolder.append(')');

        sql.append(placeHolder);

        return sql.toString() ;
    }

    public static String generateDeleteSQL(String tableName,String conditions){
        StringBuilder sqlBuilder = new StringBuilder("DELETE FROM");
        sqlBuilder.append(tableName);
        appendConditions(sqlBuilder,conditions);
        return sqlBuilder.toString();
    }

    public static void appendConditions(StringBuilder sqlBuilder,String conditions){
        if(conditions != null && !conditions.isEmpty()){
            sqlBuilder.append(" WHERE ").append(conditions);
        }
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
