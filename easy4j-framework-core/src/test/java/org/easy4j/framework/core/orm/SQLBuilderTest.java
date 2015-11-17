package org.easy4j.framework.core.orm;

import junit.framework.Assert;
import org.junit.Test;

/**
 * @author bjliuyong
 * @version 1.0
 * @created date 15-11-17
 */
public class SQLBuilderTest {

    @Test
    public void testGenerateSelectSQL() throws Exception {

    }

    @Test
    public void testGenerateSelectCountSQL() throws Exception {

    }

    @Test
    public void testGenerateSelectSqlForPager() throws Exception {

    }

    @Test
    public void testGenerateUpdateSQL() throws Exception {
        String sql01 = SQLBuilder.generateUpdateSQL("test","name=?","where id=?");
        String sql02 = SQLBuilder.generateUpdateSQL("test","set name=?","where id=?");
        String sql03 = SQLBuilder.generateUpdateSQL("test"," set name=?","where id=?");
        Assert.assertEquals("update test set name=? where id=?",sql01.trim());
        Assert.assertEquals("update test set name=? where id=?",sql02.trim());
        Assert.assertEquals("update test  set name=? where id=?",sql03.trim());
    }

    @Test
    public void testGenerateInsertSQL() throws Exception {

    }

    @Test
    public void testGenerateDeleteSQL() throws Exception {

    }
}
