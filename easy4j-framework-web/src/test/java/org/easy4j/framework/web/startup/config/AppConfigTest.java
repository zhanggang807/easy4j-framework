package org.easy4j.framework.web.startup.config;

/**
 * User: liuyong
 * Date: 15-9-24
 * Version: 1.0
 */
public class AppConfigTest {

    @org.junit.Test
    public void testGet() throws Exception {
        AppConfig.get("base.package");
        AppConfig.get("base.package");
    }


    

}
