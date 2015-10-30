package org.easy4j.framework.core.orm.sql;

import java.util.List;

/**
 * @author bjliuyong
 * @version 1.0
 * @created date 15-10-29
 */
public class ConditionBuilder {

    private static final int defaultInitCapacity = 32 ;

    private StringBuilder builder ;

    public ConditionBuilder(){
        this(defaultInitCapacity);
    }

    public ConditionBuilder(int initCapacity){
        builder = new StringBuilder(initCapacity);
    }

    public ConditionBuilder append(String condition){
        builder.append(condition);
        return this ;
    }



    public ConditionBuilder append(boolean state ,String condition){
        return state ? append(condition) : this ;
    }

    public ConditionBuilder append(boolean state ,String condition,List<Object> params ,Object param){
        if(state)
            params.add(param);
        return state ? append(condition) : this ;
    }

    public ConditionBuilder and(boolean state ,String condition ){

        if(builder.length() > 0) {
            return state ? append(" and ").append(condition) : this ;
        }

        return state ? append(condition) : this ;
    }

    public ConditionBuilder and(boolean state ,String condition ,List<Object> params ,Object param){

        if(state)
            params.add(param);
        return and(state , condition);
    }

    public ConditionBuilder or(boolean state ,String condition ){

        if(builder.length() > 0) {
            return state ? append(" or ").append(condition) : this ;
        }

        return state ? append(condition) : this ;
    }

    public String toString(){
        return builder.toString();
    }

    public static void main(String args[]){

        ConditionBuilder conditionBuilder = new ConditionBuilder();
        conditionBuilder.and(true,"sex=?")
                .and(false,"age=?")
                .and(true,"name=?")
                .or(true,"old=?");
        System.out.println(conditionBuilder);
    }

}
