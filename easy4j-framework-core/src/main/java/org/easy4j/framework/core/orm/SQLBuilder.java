package org.easy4j.framework.core.orm;


import org.easy4j.framework.core.orm.sql.*;
import org.easy4j.framework.core.util.base.Strings;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author: liuyong
 * @since 1.0
 */
public class SQLBuilder {

    public static final String ALL_COLUMNS = null ;

    public static String generateSelectSQL(String columns ,String tableName ,String conditions){
        AbstractSQL sql = new SelectSQL(tableName);
        sql.appendColumns(columns);
        sql.appendConditions(conditions);
        return sql.build();
    }

    public static String generateSelectCountSQL(String tableName ,String conditions){
        StringBuilder sql = new StringBuilder("SELECT COUNT(1) FROM ");
        sql.append(tableName);
        appendConditions(sql,conditions);
        return sql.toString();
    }

    public static String generateSelectSqlForPager(String columns ,String tableName ,String conditions ,int pageNumber,int pageSize ){
        SelectSQL sql = new SelectSQL(tableName);
        sql.appendColumns(columns);
        sql.appendConditions(conditions);
        sql.appendSqlForPager(pageNumber,pageSize);
        return sql.build();
    }

    public static String generateUpdateSQL(String tableName,String sets ,String conditions){

        if(conditions == null || conditions.length() == 0)
            return  Strings.concat("UPDATE ",tableName," SET ",sets ) ;
        else
            return  Strings.concat("UPDATE ",tableName," SET ",sets ," WHERE ",conditions) ;

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

        if(conditions == null || conditions.length() == 0)
            return Strings.concat("delete from ",tableName );
        else
            return Strings.concat("delete from ",tableName ," where ",conditions);
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
