package org.easy4j.framework.core.orm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>custom field mapping  the column name  in database </p>
 *
 * @author: liuyong
 * @since 1.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {

    /**
     *
     * this field present the column name in database
     *
     * @return
     */
    String value() default "";

    /**
     *
     * if ignore is true , this field will ignore , database operation will ignore it
     *
     * @return
     */
    boolean ignore() default false ;
}
