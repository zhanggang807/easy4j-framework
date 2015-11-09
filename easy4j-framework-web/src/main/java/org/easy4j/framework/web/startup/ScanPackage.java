package org.easy4j.framework.web.startup;

import org.easy4j.framework.core.config.Config;

/**
 * @author bjliuyong
 * @version 1.0
 * @created date 15-11-9
 */
public interface ScanPackage extends Config {

    public String[] getBasePackages() ;

    public String[] getMvcBasePackages();

    public String[] getInfrastructurePackages();
}
