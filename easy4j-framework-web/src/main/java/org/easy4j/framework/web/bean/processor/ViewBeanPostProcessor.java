package org.easy4j.framework.web.bean.processor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.web.servlet.view.velocity.VelocityConfigurer;
import org.springframework.web.servlet.view.velocity.VelocityLayoutViewResolver;

import java.util.Properties;

/**
 * @author bjliuyong
 * @version 1.0
 * @created date 15-9-23
 */
@Component
public class ViewBeanPostProcessor implements BeanPostProcessor ,BeanFactoryAware {

    private BeanFactory beanFactory ;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        if(bean instanceof VelocityLayoutViewResolver){
            return postProcessBeforeInitialization((VelocityLayoutViewResolver) bean);
        } else if (bean instanceof VelocityConfigurer) {
            return postProcessBeforeInitialization((VelocityConfigurer) bean) ;
        }

        return bean;
    }

    private Object postProcessBeforeInitialization(VelocityConfigurer velocityConfigurer){

        Properties properties = new Properties();

        Properties velocityProperties = new Properties();
        velocityProperties.setProperty("input.encoding","utf-8");
        velocityProperties.setProperty("output.encoding","utf-8");
        velocityProperties.setProperty("velocimacro.library","macro.vm");

        velocityConfigurer.setVelocityProperties(velocityProperties);

        return velocityConfigurer ;
    }

    private Object postProcessBeforeInitialization(VelocityLayoutViewResolver layoutViewResolver){
       return layoutViewResolver ;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory ;
    }
}
