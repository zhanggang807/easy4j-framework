package org.easy4j.framework.web.view;

import org.springframework.beans.factory.BeanFactory;

/**
 * @author bjliuyong
 * @version 1.0
 * @created date 15-10-13
 */
public class JspViewAdaptorProcessor implements ViewAdaptorProcessor {

    @Override
    public boolean support(String viewType) {
        return ViewType.JSP.equals(viewType);
    }

    /**
     * 发现有没有支持该视图的相关class文件
     *
     * @return
     */
    @Override
    public boolean discoverDriver() {
        return true;
    }

    /**
     * 注册ViewResolver
     */
    @Override
    public void registerViewResolver(BeanFactory beanFactory) {

    }
}
