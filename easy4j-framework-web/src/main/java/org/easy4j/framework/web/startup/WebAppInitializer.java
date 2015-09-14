package org.easy4j.framework.web.startup;

import org.easy4j.framework.core.util.ClassScanner;
import org.easy4j.framework.web.startup.config.AppConfig;
import org.easy4j.framework.web.startup.config.WebMvcConfig;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import java.io.IOException;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import java.io.IOException;
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

    public static String[] scanBasePackage ;


    static {
        ClassPathResource resource = new ClassPathResource(DEFAULT_APP_SETTING, WebAppInitializer.class);
        try {
            Properties properties = PropertiesLoaderUtils.loadProperties(resource);

            ClassPathResource appExtResource = new ClassPathResource(properties.getProperty(APP_EXT_SETTING_FILE) + ".properties" ) ;

            Properties appExtProperties = PropertiesLoaderUtils.loadProperties(appExtResource);

            String basePackage = appExtProperties.getProperty("base.package");

            if(basePackage != null && !basePackage.isEmpty()) {
                scanBasePackage = StringUtils.commaDelimitedListToStringArray(basePackage) ;
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

        if(scanBasePackage != null){
            rootAppContext.scan(scanBasePackage);
        }

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

        if(scanBasePackage != null){
            servletAppContext.scan(scanBasePackage);
        }
        servletAppContext.scan("com.man.controller" ,"com.service.util");
        Class<?>[] configClasses = getServletConfigClasses();
        if (!ObjectUtils.isEmpty(configClasses)) {
            servletAppContext.register(configClasses);
        }
        return servletAppContext;
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
}

