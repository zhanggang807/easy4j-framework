package org.easy4j.framework.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author bjliuyong
 * @version 1.0
 * @created date 15-11-4
 */
@Service
public class BeanTest {

    @Autowired
    private DaoTest daoTest;

    public void say(){
        daoTest.add();

    }

    public DaoTest getDaoTest() {
        return daoTest;
    }

    public void setDaoTest(DaoTest daoTest) {
        this.daoTest = daoTest;
    }
}
