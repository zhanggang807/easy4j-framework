package org.easy4j.framework.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * @author bjliuyong
 * @version 1.0
 * @created date 15-11-4
 */
@RunWith(TestSupport.class)
public class Easy4jTest {


    @Autowired
    private BeanTest beanTest ;

    @Test
    public void test01()throws Exception{
        beanTest.say();
        System.out.println(this);
        System.out.println(beanTest);
    }

    @Test
    public void test02()throws Exception{
        beanTest.say();
        System.out.println(this);
        System.out.println(beanTest);
    }


    public void setBeanTest(BeanTest beanTest) {
        this.beanTest = beanTest;
    }
}
