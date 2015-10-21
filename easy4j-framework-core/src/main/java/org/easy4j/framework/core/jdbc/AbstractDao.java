package org.easy4j.framework.core.jdbc;

import org.easy4j.framework.core.jdbc.handler.BeanHandler;
import org.easy4j.framework.core.jdbc.handler.BeanListHandler;
import org.easy4j.framework.core.jdbc.handler.Handlers;
import org.easy4j.framework.core.util.ReflectUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: liuyong
 * @since 1.0
 */
public abstract class AbstractDao<M> {


    protected final Class<M> beanClass;

    protected BeanHandler<M> beanHandler ;

    protected BeanListHandler<M> beanListHander ;

    protected final Map<String ,String> sqlCache  ;

    protected final Map<String,String> fieldColumnMapping ;

    protected static QueryRunner queryRunner ;

    public AbstractDao(){
        this.sqlCache  = new HashMap<String, String>();
        this.beanClass = ReflectUtils.findParameterizedType(getClass(), 0);

        this.fieldColumnMapping = JdbcUtils.getFiledAndColumnMapping(this.beanClass);

        RowProcessor rowProcessor = new BasicRowProcessor(new BeanProcessor(fieldColumnMapping)) ;

        this.beanHandler = new BeanHandler<M>(this.beanClass ,rowProcessor) ;
        this.beanListHander = new BeanListHandler<M>(this.beanClass ,rowProcessor);
    }


    //============================ select 执行sql ===========================================================
    // web 需要性能好些 用select 进行查询
    /**
     * 执行sql 查询单个bean
     * @param sql
     * @param params
     * @return
     */
    protected M selectOne(String sql, Object... params) {
        return queryRunner.query(sql,beanHandler ,params);
    }

    /**
     * 执行sql 查询集合
     * @param sql
     * @param params
     * @return
     */
    protected List<M> selectList(String sql, Object... params) {
        return queryRunner.query(sql,beanListHander,params);
    }


    /**
     * 执行插入sql
     * @param sql
     * @param params
     * @return 影响的行数
     */
    protected int insert(String sql ,Object ... params){
        return  queryRunner.insert(sql ,params);
    }

    /**
     * 执行插入sql ,返回主键
     * @param sql
     * @param returnType
     * @param params
     * @param <T>
     * @return
     */
    protected <T> T insert(String sql ,Class<T> returnType, Object ... params){
        return queryRunner.insert(sql, Handlers.getInstance(returnType) , params);
    }

}
