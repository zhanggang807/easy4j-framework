package org.easy4j.framework.web.view;

import org.easy4j.framework.web.view.velocity.VelocityViewAdaptorProcessor;

/**
 *
 * 视图适配处理器工厂
 *
 * @author bjliuyong
 * @version 1.0
 * @created date 15-10-13
 */
public class ViewAdaptorProcessorFactory {


    /**
     * 根据视图类型返回不同的视图处理器
     * @param type
     * @return
     */
    public static ViewAdaptorProcessor getInstance(String type){

        ViewAdaptorProcessor viewAdaptorProcessor ;

        if(type == null || type.isEmpty()){
            viewAdaptorProcessor =  new VelocityViewAdaptorProcessor();
            if(viewAdaptorProcessor.discoverDriver()){
                return viewAdaptorProcessor ;
            } else {
                return new JspViewAdaptorProcessor();
            }
        }

        if(type.equals(ViewType.JSP)){
            return new JspViewAdaptorProcessor();
        } else if(type.equals(ViewType.VELOCITY)){
            return new VelocityViewAdaptorProcessor();
        }


        throw new RuntimeException("can not find right ViewAdaptorProcessor class");
    }
}
