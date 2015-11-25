package org.easy4j.framework.core.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * General utility methods for working with annotations
 *
 * @author: liuyong
 * @since 1.0
 */
public class AnnotationUtils extends org.springframework.core.annotation.AnnotationUtils{
    
    public static Annotation findAnnotation(Field field ,Class<? extends Annotation> annotationType){
        Annotation[] annotations = field.getAnnotations();

        for (Annotation annotation : annotations){
            if (annotation.annotationType().equals(annotationType)){
                return annotation ;
            }
        }

        return null ;
    }


}
