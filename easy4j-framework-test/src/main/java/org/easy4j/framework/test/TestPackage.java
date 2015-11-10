package org.easy4j.framework.test;

import org.easy4j.framework.core.util.ArrayUtils;
import org.easy4j.framework.web.startup.config.DefaultScanPackage;

/**
 * @author bjliuyong
 * @version 1.0
 * @created date 15-11-9
 */
public class TestPackage  extends DefaultScanPackage {

    @Override
    public String[] getBasePackages() {

       return (String[]) ArrayUtils.addAll(super.getBasePackages() ,new String[]{"org.easy4j.framework.test.processor"}) ;
    }
}
