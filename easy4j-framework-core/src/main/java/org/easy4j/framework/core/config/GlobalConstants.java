package org.easy4j.framework.core.config;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Properties;

/**
 *
 * 全局配置
 *
 * @author bjliuyong
 * @version 1.0
 * @created date 15-10-16
 */
public class GlobalConstants {

    public static Properties easy4jProperties = new Properties();

    private static final String DEFAULT_CHARSET_NAME = "UTF-8";

    public static final String  CHARSET_NAME  = getString("project.charset.name",DEFAULT_CHARSET_NAME) ;
    public static final Charset CHARSET       =  Charset.forName(CHARSET_NAME);

    static {

        try {
            easy4jProperties.load(GlobalConstants.class.getResourceAsStream("easy4j.properties"));
        } catch (IOException e) {
            throw new RuntimeException("can not find easy4j.properties from classpath");
        }

    }

    public static String getString(String key,String defaultVal){
        if(easy4jProperties == null)
            return defaultVal ;

        String val = easy4jProperties.getProperty(key);
        return val == null ? defaultVal : val ;
    }
}
