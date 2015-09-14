package org.easy4j.framework.core;


import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * @author bjliuyong
 * @version 1.0
 * @created date 15-9-14
 */
public abstract class ClassFilter {

    List<Class<?>> classList = new ArrayList<Class<?>>();

    /** 默认全部加进去***/
    protected boolean accept(Class<?> cls){
        return true;
    }

    public void add(Class<?> cls) {
        if(accept(cls))
            classList.add(cls);
    }

    protected  List<Class<?>> getClassList(){
        return classList ;
    }

    public static ClassFilter newAnnotationFilter(final Class<? extends Annotation> annotationType) {

        return new ClassFilter() {
            @Override
            public boolean accept(Class<?> cls) {
                return cls.isAnnotationPresent(annotationType);
            }
        } ;
    }

    public static ClassFilter newSuperClassFilter(final Class<?> superClass){
        return new ClassFilter() {
            @Override
            public boolean accept(Class<?> cls) {
                return cls.isAssignableFrom(superClass);
            }
        } ;
    }
}
