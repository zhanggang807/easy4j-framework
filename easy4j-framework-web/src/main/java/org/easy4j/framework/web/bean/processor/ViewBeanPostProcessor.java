package org.easy4j.framework.web.bean.processor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
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

        Properties velocityProperties = loadDefaultVelocityConfigFile();

        velocityProperties.setProperty(SpringResourceLoader.SPRING_RESOURCE_LOADER_CACHE, "true");

        mergeVelocityConfigFile(velocityProperties);

        velocityConfigurerBuilder.addPropertyValue("velocityProperties",velocityProperties) ;

        String resourceLoaderPath = velocityProperties.getProperty("resourceLoaderPath", "classpath:tpl/");
        velocityConfigurerBuilder.addPropertyValue("resourceLoaderPath",resourceLoaderPath) ; //WEB-INF/vm
        velocityConfigurerBuilder.addPropertyValue("preferFileSystemAccess",false) ; //WEB-INF/vm

        ClassPathResource macroResource = new ClassPathResource("tpl/macro.vm");
        if(macroResource.exists()){
            velocityProperties.setProperty("velocimacro.library","macro.vm");
        }

        /* defualt<property name="exposeSpringMacroHelpers" value="false"/>*/
        /* defualt <property name="layoutKey" value="layout"/>*/
        /* defualt <property name="screenContentKey" value="screen_content"/>*/
        viewResolverBuilder.addPropertyValue("contentType","text/html;charset=" + velocityProperties.getProperty("output.encoding"));
        viewResolverBuilder.addPropertyValue("suffix",velocityProperties.getProperty("suffix",".vm")); //layoutViewResolver.setSuffix(".vm");
        viewResolverBuilder.addPropertyValue("layoutKey",velocityProperties.getProperty("suffix","layout"));
        viewResolverBuilder.addPropertyValue("exposePathVariables",velocityProperties.getProperty("exposePathVariables", "true")); //layoutViewResolver.setExposePathVariables(true);

        listableBeanFactory.registerBeanDefinition("viewResolver",viewResolverBuilder.getBeanDefinition());

        listableBeanFactory.registerBeanDefinition("velocityConfigurer",velocityConfigurerBuilder.getBeanDefinition());
    }

    private Properties loadDefaultVelocityConfigFile(){

        ClassPathResource resource = new ClassPathResource("velocity.properties", ViewBeanPostProcessor.class);
        try {
            return PropertiesLoaderUtils.loadProperties(resource);
        } catch (IOException e) {
            throw  new RuntimeException(e);
        }
    }

    /**
     * 如果classpath 下面有velocity.properties 文件 将其进行合并， 并且覆盖默认值
     * @param velocityProperties
     */
    private void mergeVelocityConfigFile(Properties velocityProperties){

        ClassPathResource classPathResource = new ClassPathResource("velocity.properties");
        if(classPathResource.exists()){
            try {
                Properties customProperties = PropertiesLoaderUtils.loadProperties(classPathResource);
                velocityProperties.putAll(customProperties);
            } catch (IOException e) {
                new RuntimeException(e);
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
