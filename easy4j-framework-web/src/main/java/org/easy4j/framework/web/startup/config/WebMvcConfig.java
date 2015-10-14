package org.easy4j.framework.web.startup.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.*;

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

        System.out.println("=====configureMessageConverters====");

    }


    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

}
