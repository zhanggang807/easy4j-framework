package org.easy4j.framework.core.jdbc.handler;

import org.easy4j.framework.core.jdbc.ResultSetHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * @author bjliuyong
 * @version 1.0
 * @created date 15-10-21
 */
public class Handlers {

    public static ResultSetHandler<Long> longHanlder = new SingleValueHandler<Long>() {};

    private static Map<Class , ResultSetHandler> handlerMap = new HashMap<Class ,ResultSetHandler>();

    static {
        handlerMap.put(Long.class,longHanlder);
    }



    public static <T> ResultSetHandler<T> getInstance(Class<T> type){
        return handlerMap.get(type) ;
    }
}
