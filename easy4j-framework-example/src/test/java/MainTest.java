import easy4j.framework.example.dao.IndexDao;
import easy4j.framework.example.entity.UserGroup;
import org.easy4j.framework.test.TestSupport;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @author bjliuyong
 * @version 1.0
 * @created date 15-11-9
 */
@RunWith(TestSupport.class)
public class MainTest  {

    @Autowired
    private IndexDao indexDao ;

    @Test
    public void test01(){
        UserGroup userGroup = new UserGroup();
        userGroup.setUid(100000);
        userGroup.setGid(120000);
        userGroup.setCreated(new Date());
        userGroup.setModified(new Date());
        Long id = indexDao.save(userGroup,Long.class);
        System.out.println(id);
    }

    public void setIndexDao(IndexDao indexDao) {
        this.indexDao = indexDao;
    }
}
