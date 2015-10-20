package org.easy4j.framework.core.jdbc;

import org.easy4j.framework.core.jdbc.filter.PropertyFilter;
import org.easy4j.framework.core.jdbc.handler.BeanHandler;
import org.easy4j.framework.core.jdbc.handler.BeanListHandler;
import org.easy4j.framework.core.jdbc.handler.SingleValueHandler;
import org.easy4j.framework.core.util.ReflectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: liuyong
 * @since 1.0
 */
public class BaseDao<M> extends AbstractDao {

    protected final Class<M> beanClass;

    protected String tableName ;

    protected String INSERT = "$insert_";

    protected ResultSetHandler<M> beanHandler ;

    protected final Map<String ,String> sqlCache  ;

    protected final Map<String,String> fieldColumnMapping ;

    protected static QueryRunner queryRunner ;

    public BaseDao(){

        this.sqlCache  = new HashMap<String, String>();
        this.beanClass = ReflectUtils.findParameterizedType(getClass(), 0);
        this.tableName = JdbcUtils.tableName(this.beanClass);
        this.fieldColumnMapping = JdbcUtils.getFiledAndColumnMapping(this.beanClass);
        this.beanHandler = new BeanHandler<M>(this.beanClass ) ;

        _initSql();
        initSql();
    }

    protected void initSql(){

    }

    /**
     * 初始化部分SQL
     */
    protected void _initSql(){

        String sql = SQLBuilder.generateInsertSQL(tableName,(String[])fieldColumnMapping.values().toArray());
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

        Object[] parameter = JdbcUtils.values(m, PropertyFilter.ID_FILTER);
        String insertSql = sql(INSERT);
        return queryRunner.insert(insertSql,parameter) > 0;
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
        return queryRunner.query(sql,new BeanListHandler<M>(beanClass),params);
    }

    public Object querySingleValue(String sql,Object... params) {
        return queryRunner.query(sql, new SingleValueHandler(), params);
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
        return queryRunner.query(sql,new BeanListHandler<M>(beanClass) ,params);
    }

}
