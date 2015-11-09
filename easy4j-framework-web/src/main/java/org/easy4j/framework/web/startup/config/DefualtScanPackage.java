package org.easy4j.framework.web.startup.config;

import org.easy4j.framework.core.config.GlobalConfig;
import org.easy4j.framework.core.util.ArrayUtils;
import org.easy4j.framework.core.util.base.Strings;
import org.easy4j.framework.web.startup.ScanPackage;


/**
 * @author bjliuyong
 * @version 1.0
 * @created date 15-11-9
 */
public class DefualtScanPackage implements ScanPackage  {


    /*********************************************************
     ** base.package
     ** base.app.package
     ** base.mvc.package
     **
     **
     *********************************************************
     */

    /**
     * base package in service
     */
    public static final String BASE_PACKAGE = "base.package" ;

    public static final String BASE_APP_PACKAGE = "base.app.package" ;

    public static final String BASE_MVC_PACKAGE = "base.mvc.package" ;

    public static final String basePackage    = GlobalConfig.getString(BASE_PACKAGE);
    public static final String baseAppPackage = GlobalConfig.getString(BASE_APP_PACKAGE);
    public static final String baseMvcPackage = GlobalConfig.getString(BASE_MVC_PACKAGE);


    @Override
    public String[] getBasePackages() {
        String[] packages01 = new String[]{basePackage + ".service",
                basePackage + ".dao" ,
                basePackage + ".manager"} ;
        return (String[])ArrayUtils.addAll(packages01 , Strings.isEmpty(baseAppPackage)? null : baseAppPackage.split(","));
    }

    @Override
    public String[] getMvcBasePackages() {
        String[] packages01 = new String[]{basePackage + ".controller" , basePackage + ".action"} ;
        return (String[])ArrayUtils.addAll(packages01 ,Strings.isEmpty(baseMvcPackage)? null :baseMvcPackage.split(","));
    }

    @Override
    public String[] getInfrastructurePackages() {
        return new String[]{"org.easy4j.framework.web.bean.processor"};
    }
}
