package org.easy4j.framework.core.orm;

import org.easy4j.framework.core.orm.annotation.Id;

/**
 * @author: liuyong
 * @since 1.0
 */
public abstract class Entity<ID> {

    @Id
    protected ID id;

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

}
