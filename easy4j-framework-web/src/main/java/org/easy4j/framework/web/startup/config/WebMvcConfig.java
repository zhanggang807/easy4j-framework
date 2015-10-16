package org.easy4j.framework.web.startup.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.*;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liuyong
 * @version 1.0
 * @created date 15-9-10
 */
@Configuration
@EnableWebMvc          //<mvc:annotation-driven />
/*@ImportResource({"classpath:spring-mvc.xml"})*/
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        System.out.println("=====addInterceptors====");

    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

        /*StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter(Charset.forName("utf-8"));
        converters.add(stringHttpMessageConverter);*/
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {

        int len = converters.size();

        for(int index = 0 ; index < len ; index++ ){
            if(converters.get(index) instanceof StringHttpMessageConverter ) {
                extendStringHttpMessageConverter(converters ,index);
            }
        }


    }


    //设置StringHttpMessageConverter编码，防止乱码
    private void extendStringHttpMessageConverter(List<HttpMessageConverter<?>> converters , int position ){
        Charset defaultCharset = Charset.forName("utf-8");
        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();

        List<MediaType> mediaTypeList = new ArrayList<MediaType>(3);

        mediaTypeList.add(new MediaType("text", "html", defaultCharset));
        mediaTypeList.add(new MediaType("text", "plain", defaultCharset));
        mediaTypeList.add(new MediaType("*", "*", defaultCharset));

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
