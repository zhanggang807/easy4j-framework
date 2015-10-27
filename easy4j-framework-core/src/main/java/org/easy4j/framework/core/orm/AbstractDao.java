package org.easy4j.framework.core.orm;

import org.easy4j.framework.core.orm.handler.*;
import org.easy4j.framework.core.util.ReflectUtils;

import java.util.List;
import java.util.Map;

/**
 *
 * web 需要性能好些 用select 进行查询
 *
 * @author: liuyong
 * @since 1.0
 */
public abstract class AbstractDao<M> {

    protected String tableName ;

    protected Class<M> entityClass ;

    protected BeanHandler<M> beanHandler ;

    protected BeanListHandler<M> beanListHander ;

    protected static QueryRunner queryRunner ;

    public AbstractDao(){
        init();
    }

    protected void init(){

        this.entityClass = ReflectUtils.findParameterizedType(getClass(), 0);
        this.tableName   = EntityMapping.getTableName(this.entityClass);
        EntityMapping.initMapping(this.entityClass);
        Map<String,String> columnFieldMapping = EntityMapping.getMapping(this.entityClass).getColumnFieldMapping();
        RowProcessor rowProcessor = new BasicRowProcessor(new BeanProcessor(columnFieldMapping)) ;

        this.beanHandler = new BeanHandler<M>(this.entityClass ,rowProcessor) ;
        this.beanListHander = new BeanListHandler<M>(this.entityClass ,rowProcessor);
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
     * 执行查询 ，得到单条记录 ，以Map返回 ， columnName===>value
     * @param sql
     * @param params
     * @return
     */
    protected Map<String,Object> selectMap(String sql ,Object ... params){
        return queryRunner.query(sql, new MapHandler(), params);
    }

    /**
     * 执行查询 ，得到多条记录 ，以List<Map>返回
     * @param sql
     * @param params
     * @return
     */
    protected List<Map<String,Object>> selectMapList(String sql ,Object ... params){
        return queryRunner.query(sql, new MapListHandler(), params);
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

    /**
     * 执行update Sql
     * @param sql
     * @param params
     * @return
     */
    protected int update(String sql ,Object ... params){
        return queryRunner.update(sql ,params);
    }



}
