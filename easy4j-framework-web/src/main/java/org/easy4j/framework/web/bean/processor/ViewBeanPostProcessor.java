package org.easy4j.framework.web.bean.processor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;
import org.springframework.ui.velocity.SpringResourceLoader;
import org.springframework.web.servlet.view.velocity.VelocityConfigurer;
import org.springframework.web.servlet.view.velocity.VelocityLayoutViewResolver;

import java.io.IOException;
import java.util.Properties;

/**
 * @author bjliuyong
 * @version 1.0
 * @created date 15-9-23
 */
@Component
public class ViewBeanPostProcessor implements BeanPostProcessor,ApplicationContextAware,BeanFactoryAware ,InitializingBean {

    private BeanFactory beanFactory ;

    private ApplicationContext applicationContext ;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        if(bean instanceof VelocityLayoutViewResolver){
            return (VelocityLayoutViewResolver) bean;
        } else if (bean instanceof VelocityConfigurer) {
            return (VelocityConfigurer) bean ;
        }

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory ;
    }

    /**
     * 集成velocity 初始化所需的bean， viewResolver ，velocityConfigurer ， 既可以完成velocity集成
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {

        DefaultListableBeanFactory listableBeanFactory = (DefaultListableBeanFactory)beanFactory ;
        BeanDefinitionBuilder viewResolverBuilder = BeanDefinitionBuilder.rootBeanDefinition(VelocityLayoutViewResolver.class);
        BeanDefinitionBuilder velocityConfigurerBuilder = BeanDefinitionBuilder.rootBeanDefinition(VelocityConfigurer.class);

        Properties velocityProperties = new Properties();

        String outputEncoding = "utf-8";

        velocityProperties.setProperty("input.encoding","utf-8");
        velocityProperties.setProperty("output.encoding",outputEncoding);
        velocityProperties.setProperty(SpringResourceLoader.SPRING_RESOURCE_LOADER_CACHE, "false");

        ClassPathResource classPathResource = new ClassPathResource("velocity.properties");
        if(classPathResource.exists()){
            try {
                Properties customProperties = PropertiesLoaderUtils.loadProperties(classPathResource);
                velocityProperties.putAll(customProperties);
            } catch (IOException e) {
                new RuntimeException(e);
            }
        }
        //velocityProperties.setProperty("velocimacro.library","macro.vm");
        velocityConfigurerBuilder.addPropertyValue("velocityProperties",velocityProperties) ;
        velocityConfigurerBuilder.addPropertyValue("resourceLoaderPath","classpath:tpl/") ; //WEB-INF/vm
        velocityConfigurerBuilder.addPropertyValue("preferFileSystemAccess",false) ; //WEB-INF/vm

        /* defualt<property name="exposeSpringMacroHelpers" value="false"/>*/
        /* defualt <property name="layoutKey" value="layout"/>*/
        /* defualt <property name="screenContentKey" value="screen_content"/>*/
        viewResolverBuilder.addPropertyValue("contentType","text/html;charset=" + velocityProperties.getProperty("output.encoding",outputEncoding));
        viewResolverBuilder.addPropertyValue("suffix",".vm"); //layoutViewResolver.setSuffix(".vm");
        viewResolverBuilder.addPropertyValue("layoutKey","layout");
        viewResolverBuilder.addPropertyValue("exposePathVariables",true); //layoutViewResolver.setExposePathVariables(true);

        listableBeanFactory.registerBeanDefinition("viewResolver",viewResolverBuilder.getBeanDefinition());

        listableBeanFactory.registerBeanDefinition("velocityConfigurer",velocityConfigurerBuilder.getBeanDefinition());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
