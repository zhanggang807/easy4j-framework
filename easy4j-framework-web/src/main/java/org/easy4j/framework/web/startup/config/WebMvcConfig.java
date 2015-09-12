package org.easy4j.framework.web.startup.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author liuyong
 * @version 1.0
 * @created date 15-9-10
 */
@Configuration
@EnableWebMvc          //<mvc:annotation-driven />
@ImportResource({"classpath:spring-mvc.xml"})
public class WebMvcConfig extends WebMvcConfigurerAdapter {

}
