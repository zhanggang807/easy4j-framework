package org.easy4j.framework.core.orm;

import java.util.HashMap;
import java.util.Map;

/**
 * @author bjliuyong
 * @version 1.0
 * @created date 15-10-23
 */
public class Mapping {

    private final String[] fields;
    private final String[] columns ;

    public Mapping(int size){
        fields = new String[size];
        columns = new String[size];
    }

    public String[] getColumns(){
        return this.columns ;
    }

    public String[] getFields(){
        return this.fields;
    }

    void put(int position ,String key ,String value){
        fields[position]     = key ;
        columns[position]    = value ;
    }

    public Map<String,String> getColumnFieldMapping(){

        Map<String,String> mapping = new HashMap<String,String>(fields.length);
        int i = 0 ;
        for(String column : columns) {

            mapping.put(column ,fields[i++]);

        }

        return mapping ;

    }

}
