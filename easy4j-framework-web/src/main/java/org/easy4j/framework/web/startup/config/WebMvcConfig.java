package org.easy4j.framework.web.startup.config;

import org.easy4j.framework.core.config.GlobalConfig;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.ClassUtils;
import org.springframework.web.servlet.config.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuyong
 * @version 1.0
 * @created date 15-9-10
 *
 * MVC 默认配置启动类
 *
 * EnableWebMvc equal <mvc:annotation-driven />
 *
 */
@Configuration
@EnableWebMvc
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    private static final String FastJsonHttpMessageConverter = "com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter" ;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        System.out.println("=====addInterceptors====");

    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {

        if(ClassUtils.isPresent(FastJsonHttpMessageConverter,this.getClass().getClassLoader())){
            try {
                converters.add((HttpMessageConverter)BeanUtils.instantiateClass(Class.forName(FastJsonHttpMessageConverter))) ;
            } catch (ClassNotFoundException e) {
                //no-op
            }
        }

        int len = converters.size();

        for(int index = 0 ; index < len ; index++ ){
            if(converters.get(index) instanceof StringHttpMessageConverter ) {
                extendStringHttpMessageConverter(converters ,index);
            }
        }


    }


    /**
     * 替换掉默认的 ， converters.remove(position); 加入新创建的
     * 设置StringHttpMessageConverter编码，防止乱码
     *
     * @param converters
     * @param position
     */
    private void extendStringHttpMessageConverter(List<HttpMessageConverter<?>> converters , int position ){
        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();

        List<MediaType> mediaTypeList = new ArrayList<MediaType>(3);

        mediaTypeList.add(new MediaType("text", "html", GlobalConfig.CHARSET));
        mediaTypeList.add(new MediaType("text", "plain", GlobalConfig.CHARSET));
        mediaTypeList.add(new MediaType("*", "*", GlobalConfig.CHARSET));

        stringHttpMessageConverter.setSupportedMediaTypes(mediaTypeList);
        stringHttpMessageConverter.setWriteAcceptCharset(false);
        converters.remove(position);
        converters.add(position,stringHttpMessageConverter);
    }


    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

}
