package org.easy4j.framework.web.view;

/**
 * @author bjliuyong
 * @version 1.0
 * @created date 15-10-13
 */
public interface ViewAdaptorProcessor {

    /**
     * 发现有没有支持该视图的相关class文件
     * @return
     */
    boolean discover();


    /**
     * 注册ViewResolver
     */
    void registerViewResolver();
}
