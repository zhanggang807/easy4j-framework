package org.easy4j.framework.web.startup;

import org.easy4j.framework.core.util.ArrayUtils;
import org.easy4j.framework.web.startup.config.AppConfig;
import org.easy4j.framework.web.startup.config.DefualtScanPackage;
import org.easy4j.framework.web.startup.config.WebMvcConfig;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;


/**
 * <p>
 *     Web启动 ， 初始化
 * </p>
 * User: liuyong
 * Date: 15-9-9
 * Version: 1.0
 */
public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    private ScanPackage scanPackage = new DefualtScanPackage();

    /**
     * {@inheritDoc}
     * <p>This implementation creates an {@link org.easy4j.framework.web.startup.AnnotationAndXmlConfigWebApplicationContext},
     * providing it the annotated classes returned by {@link #getRootConfigClasses()}.
     * Returns {@code null} if {@link #getRootConfigClasses()} returns {@code null}.
     */
    @Override
    protected WebApplicationContext createRootApplicationContext() {

        AnnotationAndXmlConfigWebApplicationContext rootAppContext = new AnnotationAndXmlConfigWebApplicationContext();

        rootAppContext.scan((String[])ArrayUtils.addAll(scanPackage.getBasePackages(),scanPackage.getInfrastructurePackages()));

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

        servletAppContext.scan(scanPackage.getMvcBasePackages());

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
    }
}

