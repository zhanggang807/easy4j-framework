package org.easy4j.framework.web.startup;

import org.easy4j.framework.core.config.Config;
import org.easy4j.framework.web.startup.config.DefaultScanPackage;

/**
 * @author bjliuyong
 * @version 1.0
 * @created date 15-11-9
 */
public interface ScanPackage extends Config {

    public static ScanPackage defualtInstance = new DefaultScanPackage();

    public static String BASE_PACKAGE_NAME = defualtInstance.getBasePackageName();

    public String getBasePackageName();

    public String[] getBasePackages() ;

    public String[] getMvcBasePackages();

    public String[] getInfrastructurePackages();
}
