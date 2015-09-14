package org.easy4j.framework.core.util;

import org.junit.After;
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
    }
}
