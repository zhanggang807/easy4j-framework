package org.easy4j.framework.core.jdbc;

import org.easy4j.framework.core.jdbc.handler.BeanHandler;
import org.easy4j.framework.core.jdbc.handler.BeanListHandler;
import org.easy4j.framework.core.util.ReflectUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: liuyong
 * @since 1.0
 */
public abstract class AbstractDao<M> {


    protected final Class<M> beanClass;

    protected String tableName ;

    protected String INSERT = "$insert_";

    protected BeanHandler<M> beanHandler ;

    protected BeanListHandler<M> beanListHander ;

    protected final Map<String ,String> sqlCache  ;

    protected final Map<String,String> fieldColumnMapping ;

    public AbstractDao(){
        this.sqlCache  = new HashMap<String, String>();
        this.beanClass = ReflectUtils.findParameterizedType(getClass(), 0);
        this.tableName = JdbcUtils.tableName(this.beanClass);
        this.fieldColumnMapping = JdbcUtils.getFiledAndColumnMapping(this.beanClass);

        RowProcessor rowProcessor = new BasicRowProcessor(new BeanProcessor(fieldColumnMapping)) ;

        this.beanHandler = new BeanHandler<M>(this.beanClass ,rowProcessor) ;
        this.beanListHander = new BeanListHandler<M>(this.beanClass ,rowProcessor);
    }

}
