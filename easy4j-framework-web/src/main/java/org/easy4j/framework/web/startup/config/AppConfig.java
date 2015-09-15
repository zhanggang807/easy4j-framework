package org.easy4j.framework.web.startup.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * @author bjliuyong
 * @version 1.0
 * @created date 15-9-11
 */
@Configuration
@ImportResource("classpath:spring/**.xml")
public class AppConfig {

}
