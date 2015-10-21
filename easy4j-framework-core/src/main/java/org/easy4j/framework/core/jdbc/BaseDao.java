package org.easy4j.framework.core.jdbc;

import org.easy4j.framework.core.jdbc.handler.BeanHandler;
import org.easy4j.framework.core.jdbc.handler.BeanListHandler;
import org.easy4j.framework.core.jdbc.handler.Handlers;
import org.easy4j.framework.core.util.ReflectUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * select 开头的函数为 直接执行sql ， sql需要自己组装 ， 其他函数不需要只需要传入条件
 *
 * @author: liuyong
 * @since 1.0
 */
public class BaseDao<M> extends AbstractDao<M> {



    protected static QueryRunner queryRunner ;

    public BaseDao(){
        super();
        _initSql();
        initSql();
    }

    protected void initSql(){

    }

    /**
     * 初始化部分SQL
     */
    protected void _initSql(){
        String[] columns = new String[fieldColumnMapping.size()];
        fieldColumnMapping.values().toArray(columns);
        String sql = SQLBuilder.generateInsertSQL(tableName,columns);
        cacheSql(INSERT , sql);

    };

    protected String sql(String key ){
        return sqlCache.get(key);
    }

    protected String cacheSql(String key ,String value){
        return sqlCache.put(key,value);
    }


    @Autowired
    public void setDataSource(DataSource dataSource) {
        if(queryRunner == null){
            queryRunner = new QueryRunner(dataSource);
        }
    }

    protected QueryRunner getQueryRunner(){
        return queryRunner;
    }

    /**
     * 增加一条记录
     * @param m
     * @return
     */
    public boolean save(M m) {

        Map<String,Object> fieldMap = JdbcUtils.getFieldMap(m, fieldColumnMapping,null);
        String insertSql = sql(INSERT);
        return insert(insertSql, fieldMap.values().toArray()) > 0;
    }

    public <T> T save(M m ,Class<T> returnType){
        Map<String,Object> fieldMap = JdbcUtils.getFieldMap(m, fieldColumnMapping,null);
        String insertSql = sql(INSERT);
        return insert(insertSql,returnType,fieldMap.values().toArray()) ;
    }

    public M queryObject(String condition ,Object ... params){
        String sql = SQLBuilder.generateSelectSQL( null , tableName , condition );
        return selectOne(sql ,params);
    }

    public List<M> queryList(String condition,Object... params) {
        String sql = SQLBuilder.generateSelectSQL( null , tableName, condition );
        return selectList(sql,params);
    }

    public List<M> queryList(int pageNumber ,int pageSize ,String condition ,Object ... params){
        String sql = SQLBuilder.generateSelectSqlForPager( null , tableName, condition , pageNumber , pageSize  );
        return selectList(sql, params);
    }

    /**
     * 查询单一数据 支持原始类型， String ,Date TimeStamp
     * @param sql
     * @param returnClass
     * @param params
     * @param <T>
     * @return
     */
    public <T> T querySingleValue(Class<T> returnClass , String sql,Object... params) {
        return queryRunner.query(sql, Handlers.getInstance(returnClass), params);
    }

    /**
     * 查询多少条数据
     * @param condition
     * @param params
     * @return Integer.MAX 足够
     */
    public int queryCount(String condition,Object ... params){
        String sql = SQLBuilder.generateSelectSQL("COUNT(1)",tableName ,condition);
        return queryRunner.query(sql,Handlers.getInstance(Integer.class),params);
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
