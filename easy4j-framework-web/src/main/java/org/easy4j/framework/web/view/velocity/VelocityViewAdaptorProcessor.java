package org.easy4j.framework.web.view.velocity;

import org.easy4j.framework.web.view.ViewAdaptorProcessor;
import org.springframework.util.ClassUtils;

/**
 * @author bjliuyong
 * @version 1.0
 * @created date 15-10-13
 */
public class VelocityViewAdaptorProcessor implements ViewAdaptorProcessor {

    /**
     * 发现有没有支持该视图的相关class文件
     *
     * @return
     */
    @Override
    public boolean discoverDriver() {

        try {
            ClassUtils.forName("org.apache.velocity.app.VelocityEngine", this.getClass().getClassLoader());
        } catch (ClassNotFoundException e) {
            return false ;
        }

        return true;
    }

    /**
     * 注册ViewResolver
     */
    @Override
    public void registerViewResolver() {

    }
}
