package org.easy4j.framework.core.orm;

import org.easy4j.framework.core.jdbc.filter.PropertyFilter;
import org.easy4j.framework.core.orm.annotation.Column;
import org.easy4j.framework.core.orm.annotation.Table;
import org.easy4j.framework.core.util.ReflectUtils;
import org.easy4j.framework.core.util.base.Strings;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.*;

/**
 * <p>
 * </p>
 * User: liuyong
 * Date: 15-3-27
 * Version: 1.0
 */
public class JdbcUtils {



    /**
     * 获取对象字段的值 ，以Object返回
     * @param obj
     * @param filter
     * @return
     */
    public static Object[] values(Object obj,PropertyFilter filter){

        List<Field> fields = ReflectUtils.findAllField(obj.getClass());
        int size = fields.size() ;

        Object[] ret = new Object[size - (filter == null ? 0:filter.size() )];
        int j = 0 ;
        for(int i = 0 ; i < size ; i++  ){
            Field field = fields.get(i);
            if(filter != null && filter.filter(field.getName()))
                continue;
            ReflectionUtils.makeAccessible(field);
            ret[j] = ReflectionUtils.getField(field, obj);
            j++ ;
        }
        return ret ;
    }

    public static Object[] getValues(Object target ,Mapping mapping){
        String[] fields = mapping.getFields();
        int len = fields.length ;
        Class targetClass = target.getClass() ;
        Object[] values = new Object[len];
        for(int i = 0 ; i < len ; i++ ){
            Field  field = ReflectionUtils.findField(targetClass,fields[i]);
            ReflectionUtils.makeAccessible(field);
            values[i] =  ReflectionUtils.getField(field,target) ;
        }
        return values ;
    }


    public static Connection getConnection(String driver, String url , String username ,String password){

        try {
            Class.forName(driver);
            return DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            //
        }

        return null ;
    }

    public static void main(String args[]) throws  Exception{

        String driver = "com.mysql.jdbc.Driver";

        Connection connection = getConnection(driver , "jdbc:mysql://localhost:3306/apns?characterEncoding=UTF-8" , "root","root");

        DatabaseMetaData databaseMetaData = connection.getMetaData();


        ResultSet rs = databaseMetaData.getTables(connection.getCatalog(), null , null, new String[]{"TABLE"});
        while(rs.next()) {

            String tableName = rs.getString("TABLE_NAME");

            ResultSet resultSet = connection.getMetaData().getColumns(connection.getCatalog(), null ,tableName, null);
            while(resultSet.next()) {
                String columnName = resultSet.getString("COLUMN_NAME");
                int dataType      = resultSet.getInt("DATA_TYPE");
                String typeName   = resultSet.getString("TYPE_NAME");
            }
        }




        long start = System.currentTimeMillis();



        for(int i = 0 ; i < 100000 ; i++ ){
            XX xx = new XX();
            BeanMap map = BeanMap.create(xx);
            xx.setFlag(i % 2 == 0);
            xx.setStatus(i);
            //Map<String ,Object> map = valuesMap(xx,null);//1244
            map.setBean(xx);
            System.out.println(map.get("flag").toString() + map.get("status").toString());

        }

        System.out.println(System.currentTimeMillis() - start);

    }

    static class PP {
        protected int mm = 10 ;
    }

    static  class XX extends PP{
        private int status ;

        private boolean flag;



        void setStatus(int status) {
            this.status = status;
        }


        void setFlag(boolean flag) {
            this.flag = flag;
        }

        int getStatus() {
            return status;
        }

        boolean isFlag() {
            return flag;
        }
    }
}
