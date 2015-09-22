package org.easy4j.framework.core.util;

import org.easy4j.framework.core.plugin.Plugin;
import org.easy4j.framework.core.plugin.PluginAdapter;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @author bjliuyong
 * @version 1.0
 * @created date 15-9-14
 */
public class ClassScannerTest {

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetClassList() throws Exception{
        ClassScanner.getClassList("org.easy4j.framework");
        List<Class<?>> classList = ClassScanner.getClassList("com.google.common.util.concurrent");
        List<Class<?>> classList02 = ClassScanner.getClassListByAnnotationType("org.easy4j.framework", TestDemo.class);
        List<Class<?>> classList03 = ClassScanner.getClassListBySuperClass("org.easy4j.framework", Plugin.class);

        Assert.assertTrue(classList02.contains(AnnotationTest.class));
        Assert.assertTrue(classList03.contains(PluginAdapter.class));
    }
}
