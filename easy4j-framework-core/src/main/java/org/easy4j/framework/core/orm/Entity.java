package org.easy4j.framework.core.orm;

import org.easy4j.framework.core.orm.annotation.Id;

/**
 * @author: liuyong
 * @since 1.0
 */
public abstract class Entity<ID> {

    @Id
    protected ID id;

    public Entity() {
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Entity{");
        sb.append("id=").append(id);
        sb.append('}');
        return sb.toString();
    }

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

}
