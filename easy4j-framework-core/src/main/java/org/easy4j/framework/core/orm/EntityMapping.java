package org.easy4j.framework.core.orm;

import org.easy4j.framework.core.orm.annotation.Column;
import org.easy4j.framework.core.orm.annotation.Table;
import org.easy4j.framework.core.util.ReflectUtils;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author bjliuyong
 * @version 1.0
 * @created date 15-10-23
 */
public class EntityMapping {


    private static Map<String,Mapping> mappings = new HashMap<String, Mapping>();

    static void initMapping(Class entityClass) {

        List<Field> fields = ReflectUtils.findAllField(entityClass);
        Mapping mapping = new Mapping(fields.size());

        int count = 0 ;;

        for(Field field : fields ){
            Annotation[] annotations = field.getAnnotations();
            boolean isAdded = false ;
            for (Annotation ann : annotations){
                if (ann.annotationType().equals(Column.class)){
                    mapping.put(count ,field.getName(),((Column)ann).value()) ;
                    isAdded = true ;
                    break;
                }
            }
            if(!isAdded){
                String columnName = field.getName();
                mapping.put(count ,columnName,JdbcUtils.translate2TableCol(columnName)) ;
            }
            count ++ ;
        }

        mappings.put(getTableName(entityClass),mapping);
    }


    public static Mapping getMapping(String tableName){
        return mappings.get(tableName);
    }

    public static Mapping getMapping(Class beanType){
        return mappings.get(getTableName(beanType));
    }

    public static String getTableName(Class beanClass){

        Table table = AnnotationUtils.findAnnotation(beanClass, Table.class);
        if(table != null){
            return table.value();
        } else {
            return JdbcUtils.translate2TableCol(beanClass.getSimpleName());
        }
    }

    final static class Mapping {

        private final String[] fields;
        private final String[] columns ;

        public Mapping(int size){
            fields = new String[size];
            columns = new String[size];
        }

        public String[] getColumns(){
            return this.columns ;
        }

        public String[] getFields(){
            return this.fields;
        }

        void put(int position ,String key ,String value){
            fields[position]     = key ;
            columns[position]    = value ;
        }

    }

}
