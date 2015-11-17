package org.easy4j.framework.core.orm;

import org.easy4j.framework.core.orm.handler.Handlers;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.util.List;

/**
 *
 * select 开头的函数为 直接执行sql ， sql需要自己组装 ， 其他函数不需要只需要传入条件
 *
 * @author: liuyong
 * @since 1.0
 */
public class BaseDao<M> extends AbstractDao<M> {

    public BaseDao(){
        super();
    }

    /**
     * 增加一条记录
     * @param m
     * @return
     */
    public boolean save(M m) {
        Mapping mapping = EntityMapping.getMapping(tableName);
        String sql = SQLBuilder.generateInsertSQL(tableName,mapping.getColumns());
        return insert(sql, JdbcUtils.getValues(m, mapping)) > 0;
    }

    /**
     * 插入数据 ，并返回自动生成的键
     * @param m
     * @param returnType  返回数据的类型 ，支持 int long string
     * @param <T>
     * @return
     */
    public <T> T save(M m ,Class<T> returnType){
        Mapping mapping = EntityMapping.getMapping(tableName);
        String sql = SQLBuilder.generateInsertSQL(tableName,mapping.getColumns());
        return insert(sql,returnType,JdbcUtils.getValues(m, mapping)) ;
    }

    public int update(M m ){
        Mapping mapping = EntityMapping.getMapping(tableName);
        Object[] values = JdbcUtils.getValues(m,mapping);

        StringBuilder sets = new StringBuilder();
        String Id  = "id" ;
        Object IdVal = null ;
        Object[] params = new Object[values.length];
        int index = 0 ;
        for(String field : mapping.getFields()){

            if(Id.equals(field)){
                IdVal = values[index];
                continue;
            }
            sets.append(field).append(" =?,");
            params[index] = values[index];
            index++ ;
        }
        sets.deleteCharAt(sets.length() - 1);
        params[index] = IdVal ;
        return update(sets.toString(), "where " + Id + "=?" ,params);
    }

    /**
     * <p>
     *     update sql
     *     eg: update("set name=? ,age=?" , "where id = ? " ,[params])
     * </p>
     * @param sets don't append `set`  eg: right === > name=?  error ====> set name=?
     * @param condition should append where  eg : right ====> where id=?  error ====> id = ?
     * @param params
     * @return
     */
    protected int update(String sets, String condition, Object... params) {
        String sql = SQLBuilder.generateUpdateSQL(tableName, sets, condition);
        return update(sql,params);
    }

    public int delete(String condition ,Object... params){
        String sql = SQLBuilder.generateDeleteSQL(tableName,condition );
        return update(sql ,params);
    }

    public M queryObject(String condition ,Object ... params){
        String sql = SQLBuilder.generateSelectSQL( SQLBuilder.ALL_COLUMNS , tableName , condition );
        return selectOne(sql ,params);
    }

    public List<M> queryList(String condition,Object... params) {
        String sql = SQLBuilder.generateSelectSQL( SQLBuilder.ALL_COLUMNS , tableName, condition );
        return selectList(sql,params);
    }

    public List<M> queryList(int pageNumber ,int pageSize ,String condition ,Object ... params){
        String sql = SQLBuilder.generateSelectSqlForPager( SQLBuilder.ALL_COLUMNS , tableName, condition , pageNumber , pageSize  );
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
        return query(sql, Handlers.getInstance(returnClass), params);
    }

    /**
     * 查询多少条数据
     * @param condition
     * @param params
     * @return Integer.MAX 足够
     */
    public int queryCount(String condition,Object ... params){
        String sql = SQLBuilder.generateSelectCountSQL(tableName ,condition);
        return query(sql,Handlers.intHandler,params);
    }


}
