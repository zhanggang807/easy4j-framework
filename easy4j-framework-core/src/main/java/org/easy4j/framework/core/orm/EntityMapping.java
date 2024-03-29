package org.easy4j.framework.core.orm;

import org.easy4j.framework.core.annotation.AnnotationUtils;
import org.easy4j.framework.core.orm.annotation.Column;
import org.easy4j.framework.core.orm.annotation.Table;
import org.easy4j.framework.core.util.ReflectUtils;
import org.easy4j.framework.core.util.base.Strings;

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

        int count = 0 ;

        for(Field field : fields ){
            Annotation annotation = AnnotationUtils.findAnnotation(field,Column.class);

            if(annotation == null ) {
                String columnName = field.getName();
                mapping.put(count++ ,columnName, Strings.humpToUnderLine(columnName)) ;
                continue;
            }

            Column column = (Column)annotation ;
            if(column.ignore()) {
                continue;
            }

            mapping.put(count++ ,field.getName(),column.value()) ;
        }

        if( count != fields.size() ) {
            mapping.reset(count);
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
            return Strings.humpToUnderLine(beanClass.getSimpleName());
        }
    }


}
