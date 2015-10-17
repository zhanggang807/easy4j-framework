package easy4j.framework.example.entity;

import org.easy4j.framework.core.orm.Entity;
import org.easy4j.framework.core.orm.annotation.Table;

import java.util.Date;

/**
 * @author: liuyong
 * @since 1.0
 */
@Table("s_user_role")
public class UserGroup extends Entity{

    private int uid ;

    private int gid ;

    private Date created ;

    private Date modified ;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }
}
