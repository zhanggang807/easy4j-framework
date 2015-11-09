package org.easy4j.framework.test;

import org.easy4j.framework.core.util.ArrayUtils;
import org.easy4j.framework.web.startup.ScanPackage;
import org.easy4j.framework.web.startup.config.DefualtScanPackage;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import org.springframework.context.ApplicationContext;


/**
 * @author bjliuyong
 * @version 1.0
 * @created date 15-11-4
 */
public class TestSupport extends BlockJUnit4ClassRunner {

    private static  AnnotationAndXmlApplicationContext  applicationContext ;

    public TestSupport(Class<?> klass) throws InitializationError {
        super(klass);

    }

    public synchronized ApplicationContext initApplicationContext(){
        if(applicationContext != null )
            return applicationContext ;
        applicationContext = new AnnotationAndXmlApplicationContext();

        ScanPackage scanPackage = new TestPackage();
        applicationContext.scan(scanPackage.getBasePackages());
        applicationContext.refresh();

        return applicationContext;
    }


    @Override
    protected Object createTest() throws Exception {
        Object testInstance = super.createTest();
        ApplicationContext applicationContext = initApplicationContext();
        applicationContext.getAutowireCapableBeanFactory().autowireBean(testInstance);
        return testInstance;
    }


}
