package org.easy4j.framework.web.startup;

import org.easy4j.framework.web.startup.config.AppConfig;
import org.easy4j.framework.web.startup.config.WebMvcConfig;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
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

    public static String scanBasePackage ;

    public static String[] scanExtMvcPackages ;
    public static String[] scanExtAppPackages ;


    static {
        scanBasePackage    = AppConfig.get(AppConfig.BASE_PACKAGE);
        String appPackages = AppConfig.get(AppConfig.BASE_APP_PACKAGE);
        String mvcPackages = AppConfig.get(AppConfig.BASE_MVC_PACKAGE);

        if(mvcPackages != null && mvcPackages.length() > 0){
            scanExtMvcPackages = StringUtils.commaDelimitedListToStringArray(mvcPackages);
        }
        appPackages  =
                appPackages == null ?
                        "org.easy4j.framework.web.bean.processor" : ("org.easy4j.framework.web.bean.processor," + appPackages) ;

        scanExtAppPackages = StringUtils.commaDelimitedListToStringArray(appPackages);
    }


    /**
     * {@inheritDoc}
     * <p>This implementation creates an {@link org.easy4j.framework.web.startup.AnnotationAndXmlConfigWebApplicationContext},
     * providing it the annotated classes returned by {@link #getRootConfigClasses()}.
     * Returns {@code null} if {@link #getRootConfigClasses()} returns {@code null}.
     */
    @Override
    protected WebApplicationContext createRootApplicationContext() {

        AnnotationAndXmlConfigWebApplicationContext rootAppContext = new AnnotationAndXmlConfigWebApplicationContext();

        rootAppContext.setConfigLocation("classpath:spring-**.xml,classpath*:spring-config.xml");

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

    /**
     * Specify filters to add and map to the {@code DispatcherServlet}.
     * @return an array of filters or {@code null}
     * @see #registerServletFilter(ServletContext, javax.servlet.Filter)
     */
    protected Filter[] getServletFilters() {

        CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
        encodingFilter.setEncoding("utf-8");

        HiddenHttpMethodFilter hiddenHttpMethodFilter = new HiddenHttpMethodFilter();

        return new Filter[]{encodingFilter ,hiddenHttpMethodFilter};
    }

    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {

        //clear
        scanBasePackage = null ;
        scanExtMvcPackages = null ;
        scanExtAppPackages = null ;
    }
}

