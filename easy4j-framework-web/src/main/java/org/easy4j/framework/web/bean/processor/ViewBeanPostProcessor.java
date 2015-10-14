package org.easy4j.framework.web.bean.processor;

import org.easy4j.framework.web.view.ViewAdaptorProcessor;
import org.easy4j.framework.web.view.ViewAdaptorProcessorFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.velocity.VelocityConfigurer;
import org.springframework.web.servlet.view.velocity.VelocityLayoutViewResolver;


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

            /*if(applicationContext.containsBean("velocityTools"))
                ((VelocityLayoutViewResolver) bean ).setAttributesMap((Map)applicationContext.getBean("velocityTools"));*/
            return  bean;
        } else if (bean instanceof VelocityConfigurer) {
            return  bean ;
        }

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }



    /**
     * 集成velocity 初始化所需的bean， viewResolver ，velocityConfigurer ， 既可以完成velocity集成
     *
     * 如果引入了velocity jar包 ，并且easy4j.view 未指定特定的视图 ， 那么就使用velocity
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {

        ViewAdaptorProcessor viewAdaptorProcessor =  ViewAdaptorProcessorFactory.getInstance("");
        viewAdaptorProcessor.registerViewResolver(beanFactory);

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory ;
    }
}
