package org.easy4j.framework.core.orm.sql;

/**
 * @author bjliuyong
 * @version 1.0
 * @created date 15-10-29
 */
public class ConditionBuilder {

    private static final int defaultInitCapacity = 256 ;

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

    public ConditionBuilder andContions(boolean state ,String condition ){
        if(builder.length() > 0)
            return state ? append(" and ").append(condition) : this ;
        else
            return state ? append(condition) : this ;
    }

    public ConditionBuilder orContions(boolean state ,String condition ){
        if(builder.length() > 0)
            return state ? append(" or ").append(condition) : this ;
        else
            return state ? append(condition) : this ;
    }

    public String toString(){
        return builder.toString();
    }

    public static void main(String args[]){

        ConditionBuilder conditionBuilder = new ConditionBuilder();
        conditionBuilder.andContions(true,"sex=?")
                .andContions(false,"age=?")
                .andContions(true,"name=?");

        System.out.println(conditionBuilder);
    }

}
