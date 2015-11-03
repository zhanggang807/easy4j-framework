package org.easy4j.framework.core.orm.sql;

import java.util.List;

/**
 * @author bjliuyong
 * @version 1.0
 * @created date 15-10-29
 */
public class ConditionBuilder {

    private static final String AND = " and ";
    private static final String OR = " or ";

    private StringBuilder builder ;


    public static ConditionBuilder newInstance(){
        return new ConditionBuilder();
    }

    public static ConditionBuilder newInstance(int initCapacity){
        return new ConditionBuilder(initCapacity);
    }



    private ConditionBuilder(){
        builder = new StringBuilder();
    }

    private ConditionBuilder(int initCapacity){
        builder = new StringBuilder(initCapacity);
    }

    public ConditionBuilder append(String condition){

        if(builder.length() > 0){
            builder.append(condition);
        } else {
            builder.append("where ").append(condition);
        }
        return this ;
    }

    public ConditionBuilder append(boolean state ,String condition){
        return state ? append(condition) : this ;
    }

    public ConditionBuilder append(boolean state ,String condition,List<Object> params ,Object param){
        if(state)
            params.add(param);
        return append(state,condition) ;
    }

    public ConditionBuilder and(String codition){

        if(builder.length() > 0){
            return append(AND).append(codition);
        } else {
            return append(codition);
        }

    }

    public ConditionBuilder and(boolean state ,String condition ){

        if(builder.length() > 0) {
            return state ? append(AND).append(condition) : this ;
        }

        return state ? append(condition) : this ;
    }

    public ConditionBuilder and(boolean state ,String condition ,List<Object> params ,Object param){

        if(state)
            params.add(param);
        return and(state, condition);
    }

    public ConditionBuilder or(boolean state ,String condition ){

        if(builder.length() > 0) {
            return state ? append(OR).append(condition) : this ;
        }

        return state ? append(condition) : this ;
    }

    public String toString(){
        return builder.toString();
    }

    public static void main(String args[]){

        ConditionBuilder conditionBuilder = new ConditionBuilder();
        conditionBuilder.and(true, "sex=?")
                .and(false, "age=?")
                .and(true, "name=?")
                .or(true, "old=?");
        System.out.println(conditionBuilder);
    }

}
