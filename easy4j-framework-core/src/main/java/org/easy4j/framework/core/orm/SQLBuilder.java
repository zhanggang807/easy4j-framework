package org.easy4j.framework.core.orm;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author: liuyong
 * @since 1.0
 */
public class SQLBuilder {

    public static final String ALL_COLUMNS = null ;

    public static String generateSelectSQL(String columns ,String tableName ,String conditions){
        if(columns == null || columns.length() == 0) {
            return join("select * from",tableName ,conditions);
        }

        return join("select",columns ,"from" ,tableName ,conditions);
    }


    public static String generateSelectCountSQL(String tableName ,String conditions){
        return join("select count(1) from" ,tableName ,conditions);
    }

    public static String generateSelectSqlForPager(String columns ,String tableName ,String conditions ,int pageNumber,int pageSize ){

        int start = 0 ;

        if(pageNumber > 1)
            start = (pageNumber - 1) * pageSize ;

        StringBuilder limitBuilder = new StringBuilder(5);
        limitBuilder.append("limit ").append(start).append(',').append(pageNumber);

        if(columns == null || columns.length() == 0) {
            return join("select * from",tableName,conditions,limitBuilder.toString());
        }
        return join("select",columns,"from",tableName,conditions,limitBuilder.toString());
    }


    public static String generateUpdateSQL(String tableName,String sets ,String conditions){
        return  join("update",tableName,"set",sets ,conditions) ;
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
        return join("delete from",tableName ,conditions);
    }

    public static String join(String ... params){
        int len =  0 ;

        for (String str : params){

            if(str == null || str.length() == 0)
                continue;

            len = len + 1 + str.length() ; //1 for ' '
        }
        char[] chars = new char[len];

        int pos = 0 ;
        for(String str : params){

            if(str == null || str.length() == 0)
                continue;
            int l = str.length();
            str.getChars(0, l, chars, pos);
            pos += l ;
            chars[pos++] = ' ';
        }

        return new String(chars);
    }

    public static void main(String args[]){

        System.out.println(join("select","age,sex","from","t1"));

        System.out.println(generateSelectSQL("","user","where id=?"));
        System.out.println(generateSelectSQL(null,"user","where id=?"));
        System.out.println(generateSelectSQL("id,name,age","user",""));
        System.out.println(generateSelectSQL("id,name,age", "user", null));
        System.out.println(generateDeleteSQL("user", null));
        System.out.println(generateDeleteSQL("user", "where id=?"));
        System.out.println(generateInsertSQL("user", "id", "name", "age"));
        Map<String,String> fieldMapping = new LinkedHashMap<String, String>(10);
        fieldMapping.put("id","id");
        fieldMapping.put("name","name");
        fieldMapping.put("age","age");

        System.out.println(generateInsertSQL("user", fieldMapping.values().toArray()));
    }



}
