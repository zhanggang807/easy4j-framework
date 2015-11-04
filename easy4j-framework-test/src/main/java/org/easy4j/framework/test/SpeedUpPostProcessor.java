package org.easy4j.framework.test;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * @author bjliuyong
 * @version 1.0
 * @created date 15-11-4
 */
@Component
public class SpeedUpPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

        if(!(beanFactory instanceof DefaultListableBeanFactory)) {
            throw new RuntimeException("if speed up spring, bean factory must be type of DefaultListableBeanFactory");
        }

        DefaultListableBeanFactory listableBeanFactory = (DefaultListableBeanFactory) beanFactory;

        for(String beanName : listableBeanFactory.getBeanDefinitionNames()) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);

            beanDefinition.setLazyInit(true);
        }

    }
}
