package org.easy4j.framework.core.orm;

import org.easy4j.framework.core.orm.annotation.Column;
import org.junit.Test;

/**
 * @author: liuyong
 * @since 1.0
 */
public class EntityMappingTest {


    private String name ;

    @Column(ignore = true)
    private String _name ;

    @Column("age")
    private int _age;

    @Test
    public void testGetMapping() throws Exception {
        EntityMapping.initMapping(EntityMappingTest.class);
        Mapping mapping = EntityMapping.getMapping("entity_mapping_test");
        System.currentTimeMillis();
    }



    @Test
    public void testGetTableName() throws Exception {

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public int get_age() {
        return _age;
    }

    public void set_age(int _age) {
        this._age = _age;
    }
}
