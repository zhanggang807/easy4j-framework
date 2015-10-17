package org.easy4j.framework.core.jdbc;

import org.easy4j.framework.core.jdbc.filter.PropertyFilter;
import org.easy4j.framework.core.orm.annotation.Column;
import org.easy4j.framework.core.orm.annotation.Table;
import org.easy4j.framework.core.util.ReflectUtils;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * </p>
 * User: liuyong
 * Date: 15-3-27
 * Version: 1.0
 */
public class JdbcUtils {


    public static String tableName(Class beanClass){

        Table table = AnnotationUtils.findAnnotation(beanClass, Table.class);
        if(table != null){
            return table.value();
        } else {
            return translate2TableCol(beanClass.getSimpleName());
        }
    }

    public static String pk(Class beanClass){

        return "id" ;
        /*Field pk = null ;

        Field[] fields = beanClass.getDeclaredFields();
        for(Field field : fields ){
            Annotation[] annotations = field.getAnnotations();
            for (Annotation ann : annotations){
                if (ann.annotationType().equals(Id.class)){
                    pk = field ;
                    break;
                }
            }
        }
        if (pk == null)
            return "id" ;
        return translate2TableCol(pk.getName());*/
    }

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

    /**
     * 获取对象字段的值 ，以Map返回
     * @param obj
     * @param filter
     * @return
     */
    public static Map<String ,Object> valuesMap(Object obj,PropertyFilter filter){

        List<Field> fields = ReflectUtils.findAllField(obj.getClass());
        int size = fields.size() ;

        Map<String ,Object> ret  =  new HashMap<String, Object>();
        for(int i = 0 ; i < size ; i++  ){
            Field field = fields.get(i);
            String fieldName = field.getName() ;
            if(filter != null && filter.filter(field.getName()))
                continue;
            ReflectionUtils.makeAccessible(field);
            ret.put(translate2TableCol(fieldName) , ReflectionUtils.getField(field, obj));
        }
        return ret ;
    }


    public static String[] columns(Class beanClass){

        List<String> columns = new ArrayList<String>();

        List<Field> fields = ReflectUtils.findAllField(beanClass);
        for(Field field : fields ){
            Annotation[] annotations = field.getAnnotations();
            boolean isAdded = false ;
            for (Annotation ann : annotations){
                if (ann.annotationType().equals(Column.class)){
                    columns.add(((Column)ann).value());
                    isAdded = true ;
                    break;
                }
            }
            if(!isAdded){
                String columnName = field.getName();
                columns.add(translate2TableCol(columnName));
            }

        }
        String[] ret = new String[columns.size()] ;
        columns.toArray(ret) ;
        return ret;
    }

    private static String translate2TableCol(String columnName){
        columnName.toLowerCase();
        StringBuilder sb = new StringBuilder(columnName.length());
        char[] chars = columnName.toCharArray() ;

        char c = chars[0];
        if(c >= 'A' && c <= 'Z'){
            sb.append((char)(c| 0x20));
        } else {
            sb.append(c);
        }

        int len = chars.length ;

        for(int i = 1 ; i < len ; i++) {
            c = chars[i];
            if(  c >= 'A' && c <= 'Z'){
                sb.append("_");
                sb.append((char)(c| 0x20));
            } else {
                sb.append(c);
            }
        }

        return sb.toString() ;
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
