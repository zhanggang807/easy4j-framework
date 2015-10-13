package org.easy4j.framework.web.view;

import org.easy4j.framework.web.view.velocity.VelocityViewAdaptorProcessor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;

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
     * @param viewType
     * @return
     */
    public static ViewAdaptorProcessor getInstance(String viewType){

        Map<String,ViewAdaptorProcessor>  container = new HashMap<String,ViewAdaptorProcessor>();
        registerProcessor(container ,new VelocityViewAdaptorProcessor());
        registerProcessor(container ,new JspViewAdaptorProcessor());

        ServiceLoader<ViewAdaptorProcessor> viewAdaptorProcessors =   ServiceLoader.load(ViewAdaptorProcessor.class);

        Iterator<ViewAdaptorProcessor> iterator =  viewAdaptorProcessors.iterator();

        while (iterator.hasNext()){
            registerProcessor(container,iterator.next());
        }

        for(ViewAdaptorProcessor viewAdaptorProcessor : container.values()){
            //如果没有指定特定的视图 ，自动适配
            if(viewAdaptorProcessor.support(viewType) && viewAdaptorProcessor.discoverDriver() ){
                return viewAdaptorProcessor;
            }

        }

        return new JspViewAdaptorProcessor();
    }

    public static void registerProcessor(Map<String,ViewAdaptorProcessor> container, ViewAdaptorProcessor viewAdaptorProcessor){
        container.put(viewAdaptorProcessor.getClass().getSimpleName(),viewAdaptorProcessor);
    }
}
