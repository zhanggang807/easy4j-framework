package org.easy4j.framework.web.view.velocity;

import org.easy4j.framework.core.config.GlobalConfig;
import org.easy4j.framework.core.util.base.Strings;
import org.easy4j.framework.web.bean.processor.VelocityConstants;
import org.easy4j.framework.web.startup.config.AppConfig;
import org.easy4j.framework.web.view.ViewAdaptorProcessor;
import org.easy4j.framework.web.view.ViewType;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.ui.velocity.SpringResourceLoader;
import org.springframework.util.ClassUtils;
import org.springframework.web.servlet.view.velocity.VelocityConfigurer;
import org.springframework.web.servlet.view.velocity.VelocityLayoutViewResolver;

import java.io.IOException;
import java.util.Properties;

/**
 * @author bjliuyong
 * @version 1.0
 * @created date 15-10-13
 */
public class VelocityViewAdaptorProcessor implements ViewAdaptorProcessor {

    @Override
    public boolean support(String viewType) {

        if(Strings.isBlank(viewType))
            return true ;

        return ViewType.VELOCITY.equalsIgnoreCase(viewType);
    }

    /**
     * 发现有没有支持该视图的相关class文件
     *
     * @return
     */
    @Override
    public boolean discoverDriver() {
        return ClassUtils.isPresent("org.apache.velocity.app.VelocityEngine", this.getClass().getClassLoader());
    }

    /**
     * 注册ViewResolver
     */
    @Override
    public void registerViewResolver(BeanFactory beanFactory) {
        DefaultListableBeanFactory listableBeanFactory = (DefaultListableBeanFactory)beanFactory ;
        BeanDefinitionBuilder viewResolverBuilder = BeanDefinitionBuilder.rootBeanDefinition(VelocityLayoutViewResolver.class);
        BeanDefinitionBuilder velocityConfigurerBuilder = BeanDefinitionBuilder.rootBeanDefinition(VelocityConfigurer.class);

        Properties velocityProperties = loadDefaultVelocityConfigFile();

        velocityProperties.setProperty(SpringResourceLoader.SPRING_RESOURCE_LOADER_CACHE, "true");
        velocityProperties.setProperty(SpringResourceLoader.SPRING_RESOURCE_LOADER_CACHE, "false");

        mergeVelocityConfigFile(velocityProperties);

        String resourceLoaderPath = velocityProperties.getProperty(VelocityConstants.RESOURCE_LOADER_PATH);

        initMacro(velocityProperties);

        velocityConfigurerBuilder.addPropertyValue("resourceLoaderPath",resourceLoaderPath) ; //WEB-INF/vm
        velocityConfigurerBuilder.addPropertyValue("preferFileSystemAccess",false) ; //WEB-INF/vm
        velocityConfigurerBuilder.addPropertyValue("velocityProperties",velocityProperties) ;


        viewResolverBuilder.addPropertyValue("contentType","text/html;charset=" + velocityProperties.getProperty("output.encoding"));
        viewResolverBuilder.addPropertyValue("suffix",velocityProperties.getProperty("suffix",".vm")); //layoutViewResolver.setSuffix(".vm");
        viewResolverBuilder.addPropertyValue("layoutKey",velocityProperties.getProperty("layoutKey","layout"));
        viewResolverBuilder.addPropertyValue("layoutUrl",velocityProperties.getProperty(VelocityConstants.VELOCITY_LAYOUT_URL,
                VelocityConstants.VELOCITY_LAYOUT_URL_VALUE));
        viewResolverBuilder.addPropertyValue("exposePathVariables",velocityProperties.getProperty("exposePathVariables", "true")); //layoutViewResolver.setExposePathVariables(true);

        if(listableBeanFactory.containsBean("velocityTools")){
            viewResolverBuilder.addPropertyReference("attributesMap","velocityTools");
        }

        listableBeanFactory.registerBeanDefinition("viewResolver",viewResolverBuilder.getBeanDefinition());

        listableBeanFactory.registerBeanDefinition("velocityConfigurer",velocityConfigurerBuilder.getBeanDefinition());
    }



    //org/apache/velocity/runtime/defaults/velocity.properties
    private Properties loadDefaultVelocityConfigFile(){

        Properties velocityProperties = new Properties();
        velocityProperties.put("input.encoding", GlobalConfig.CHARSET_NAME);
        velocityProperties.put("output.encoding", GlobalConfig.CHARSET_NAME);
        velocityProperties.put("resource.loader.path","/WEB-INF/vm,classpath:tpl/");

        String layoutUrl = GlobalConfig.getString(VelocityConstants.VELOCITY_LAYOUT_URL);

        if(!Strings.isBlank(layoutUrl)){
            velocityProperties.put(VelocityConstants.VELOCITY_LAYOUT_URL,layoutUrl);
        }

        return velocityProperties;
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

    /**
     * 设置是否有宏 ， 默认路劲在tpl/macro.vm下面
     * @param velocityProperties
     */
    private void initMacro(Properties velocityProperties){
        ClassPathResource macroResource = new ClassPathResource("tpl/macro.vm");
        if(macroResource.exists()){
            velocityProperties.setProperty("velocimacro.library","macro.vm");
        }
    }


}
