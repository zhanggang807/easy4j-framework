package org.easy4j.framework.web.startup.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.io.support.PropertiesLoaderSupport;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.Properties;

/**
 * @author bjliuyong
 * @version 1.0
 * @created date 15-9-11
 */
@Configuration
@ImportResource({"classpath:spring-**.xml"})
public class AppConfig  {

    private static Properties appCustomConfig = new Properties();

    public static final String BASE_PACKAGE = "base.package" ;
    public static final String BASE_APP_PACKAGE = "base.app.package" ;
    public static final String BASE_MVC_PACKAGE = "base.mvc.package" ;

    /**
     * 可以通过配置easy4j.view = jsp(velocity)， 强制使用具体的视图 ，如果没有配置该属性， 自动根据引入的jar适配视图
     */
    public static final String EASY4J_VIEW  = "easy4j.view" ;


    static {
        try {
            appCustomConfig = PropertiesLoaderUtils.loadAllProperties("easy4j.properties");
        } catch (IOException e) {
            //no-op
        }
    }

    public static String get(String key ){
        if(appCustomConfig == null) {
            return null ;
        }
        return appCustomConfig.getProperty(key);
    }

    public static String get(String key ,String defaultVal ){
        if(appCustomConfig == null ){
            return defaultVal;
        }
        return appCustomConfig.getProperty(key,defaultVal);
    }

    /**
     * //未指定视图 ，可以适应任意视图
     * @param viewType @see ViewType
     * @return
     */
    public static boolean canAdapterView(String viewType){
        String view = get(EASY4J_VIEW);

        if(view == null)
            return true ;

        return viewType.equals(view) ;
    }

    /**
     * clear cache
     */
    public static void clear(){
        appCustomConfig.clear();
        appCustomConfig = null ;
    }

}
