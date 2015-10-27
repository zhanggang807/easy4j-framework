package easy4j.framework.example.dao;

import org.easy4j.framework.core.orm.BaseDao;
import org.springframework.stereotype.Service;

/**
 * @author bjliuyong
 * @version 1.0
 * @created date 15-10-27
 */
@Service
public class DbDao extends BaseDao {

    protected void init(){
       System.out.println("======DbDao======= init()");
    }

}
