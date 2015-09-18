package org.easy4j.framework.web.startup;

import org.easy4j.framework.web.startup.config.AppConfig;
import org.easy4j.framework.web.startup.config.WebMvcConfig;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.ServletRegistration;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;


/**
 * <p>
 *     Web启动 ， 初始化
 * </p>
 * User: liuyong
 * Date: 15-9-9
 * Version: 1.0
 */
public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    private static final String  DEFAULT_APP_SETTING = "WebAppInitializer.properties";

    /**
     * 应用默认扩展文件名称
     */
    public static final String APP_EXT_SETTING_FILE = "base.setting.filename";

    public static String scanBasePackage ;

    public static String[] scanExtMvcPackages ;
    public static String[] scanExtAppPackages ;


    static {
        ClassPathResource resource = new ClassPathResource(DEFAULT_APP_SETTING, WebAppInitializer.class);
        try {
            Properties properties = PropertiesLoaderUtils.loadProperties(resource);

            ClassPathResource appExtResource = new ClassPathResource(properties.getProperty(APP_EXT_SETTING_FILE) + ".properties" ) ;

            Properties appExtProperties = PropertiesLoaderUtils.loadProperties(appExtResource);

            scanBasePackage = appExtProperties.getProperty("base.package");

            String mvcPackages = appExtProperties.getProperty("base.mvc.package");
            String appPackages = appExtProperties.getProperty("base.app.package");

            if(mvcPackages != null && mvcPackages.length() > 0){
                scanExtMvcPackages = StringUtils.commaDelimitedListToStringArray(mvcPackages);
            }

            if(appPackages != null && appPackages.length() > 0){
                scanExtAppPackages = StringUtils.commaDelimitedListToStringArray(appPackages);
            }

        } catch (IOException ex) {
            throw new IllegalStateException("Could not load 'WebAppInitializer.properties': " + ex.getMessage());
        }
    }


    /**
     * {@inheritDoc}
     * <p>This implementation creates an {@link org.springframework.web.context.support.AnnotationConfigWebApplicationContext},
     * providing it the annotated classes returned by {@link #getRootConfigClasses()}.
     * Returns {@code null} if {@link #getRootConfigClasses()} returns {@code null}.
     */
    @Override
    protected WebApplicationContext createRootApplicationContext() {

        AnnotationConfigWebApplicationContext rootAppContext = new AnnotationConfigWebApplicationContext();

        register(rootAppContext ,
                new String[]{   scanBasePackage + ".service" ,
                                scanBasePackage + ".dao" ,
                                scanBasePackage + ".manager"} ,scanExtAppPackages);

        Class<?>[] configClasses = getRootConfigClasses();
        if (!ObjectUtils.isEmpty(configClasses)) {
            rootAppContext.register(configClasses);
        }

        return rootAppContext;
    }


    /**
     * {@inheritDoc}
     * <p>This implementation creates an {@link AnnotationConfigWebApplicationContext},
     * providing it the annotated classes returned by {@link #getServletConfigClasses()}.
     */
    @Override
    protected WebApplicationContext createServletApplicationContext() {
        AnnotationConfigWebApplicationContext servletAppContext = new AnnotationConfigWebApplicationContext();

        register(servletAppContext , new String[]{
                scanBasePackage + ".controller" , scanBasePackage + ".action"} ,scanExtMvcPackages);

        Class<?>[] configClasses = getServletConfigClasses();
        if (!ObjectUtils.isEmpty(configClasses)) {
            servletAppContext.register(configClasses);
        }
        return servletAppContext;
    }

    private void register(AnnotationConfigWebApplicationContext annotationConfigWebApplicationContext,String basePackage[] , String[] extScanPackages){

        int baseLen = basePackage == null ? 0 : basePackage.length ;
        int extLen = extScanPackages == null ? 0 : extScanPackages.length ;

        if(baseLen + extLen == 0)
            return;

        String[] scanPackages = new String[(baseLen + extLen)];

        if(basePackage != null) {
            System.arraycopy(basePackage ,0  ,scanPackages ,0 ,baseLen);
        }

        if(extScanPackages != null){
            System.arraycopy(extScanPackages ,0  ,scanPackages ,baseLen ,extLen);
        }

        if(scanPackages.length > 0 ){
            annotationConfigWebApplicationContext.scan(scanPackages);
        }

    }


    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{AppConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{WebMvcConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
    }
}

