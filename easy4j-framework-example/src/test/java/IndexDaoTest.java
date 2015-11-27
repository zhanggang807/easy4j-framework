import easy4j.framework.example.dao.IndexDao;
import easy4j.framework.example.entity.UserGroup;
import org.easy4j.framework.test.TestSupport;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * @author bjliuyong
 * @version 1.0
 * @created date 15-11-9
 */
@RunWith(TestSupport.class)
public class IndexDaoTest  {

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

        userGroup.setId(32);
        userGroup.setUid(110);
        int i = indexDao.update(userGroup);

        System.out.println(id);
    }

    @Test
    public void testQuery(){
        UserGroup userGroup = indexDao.queryObject("where uid = ?", 100000);
        System.out.println(userGroup);
        List<UserGroup> userGroupList = indexDao.queryList(1, 10, "where created < ?", new Date());
        System.out.println(userGroupList.size());
        for (UserGroup userGroup1 : userGroupList){
            System.out.println(userGroup1);
        }
    }

    public void setIndexDao(IndexDao indexDao) {
        this.indexDao = indexDao;
    }
}
