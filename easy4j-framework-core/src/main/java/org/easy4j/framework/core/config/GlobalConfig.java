package org.easy4j.framework.core.config;

import java.io.IOException;
import java.io.InputStream;
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
public class GlobalConfig {

    private static Properties easy4jProperties = new Properties();

    private static final String DEFAULT_CHARSET_NAME = "UTF-8";

    public static final String  CHARSET_NAME  = getString("project.charset.name",DEFAULT_CHARSET_NAME) ;
    public static final Charset CHARSET       =  Charset.forName(CHARSET_NAME);

    static {

        InputStream inputStream = null;
        try {
            inputStream = GlobalConfig.class.getClassLoader().getResourceAsStream("easy4j.properties");
            easy4jProperties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("can not find easy4j.properties from classpath");
        } finally {
            if(inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    //no-op
                }
            }
        }

    }

    /**
     * 获取全局配置 key 所对应的值
     * @param key
     * @param defaultVal
     * @return
     */
    public static String getString(String key,String defaultVal){
        if(easy4jProperties == null)
            return defaultVal ;

        String val = easy4jProperties.getProperty(key);
        return val == null ? defaultVal : val ;
    }

    /**
     *  当key不在需要了 ， 删除掉 ，用完就删掉无用的 ，节省内存
     * @param key
     */
    public static void remove(String key){
        easy4jProperties.remove(key);
    }
}
