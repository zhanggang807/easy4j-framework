package org.easy4j.framework.web.view;

import org.springframework.beans.factory.BeanFactory;

/**
 * @author bjliuyong
 * @version 1.0
 * @created date 15-10-13
 */
public interface ViewAdaptorProcessor {

    /**
     * 判断是否支持该视图
     *
     * @param viewType
     * @return
     */
    boolean support(String viewType);

    /**
     * 发现有没有支持该视图的相关class文件
     * @return
     */
    boolean discoverDriver();


    /**
     * 注册ViewResolver
     */
    void registerViewResolver(BeanFactory beanFactory);
}
