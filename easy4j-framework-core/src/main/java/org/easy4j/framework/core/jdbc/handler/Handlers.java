package org.easy4j.framework.core.jdbc.handler;

import org.easy4j.framework.core.jdbc.ResultSetHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * @author bjliuyong
 * @version 1.0
 * @created date 15-10-21
 */
public class Handlers{

    public static ResultSetHandler<Long> longHandler = new SingleValueHandler<Long>() {};
    public static ResultSetHandler<Integer> intHandler = new SingleValueHandler<Integer>() {};
   // public static ResultSetHandler<String> stringHandler = new SingleValueHandler<String>() {};

    private static Map<Class , ResultSetHandler> handlerMap = new HashMap<Class ,ResultSetHandler>();

    static {
        handlerMap.put(Long.class,longHandler);
        handlerMap.put(Integer.class,intHandler);
        //handlerMap.put(String.class ,stringHandler) ;
    }



    public static <T> ResultSetHandler<T> getInstance(Class<T> type){
        return handlerMap.get(type) ;
    }

    public static <T> void addHandler(ResultSetHandler<T> handler ){

    }
}
