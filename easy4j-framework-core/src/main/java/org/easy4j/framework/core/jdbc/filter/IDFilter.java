package org.easy4j.framework.core.jdbc.filter;

/**
 * <p>
 *     属性过滤器 ， 只过滤属性为ID的
 * </p>
 * User: liuyong
 * Date: 15-1-18
 * Version: 1.0
 */
public class IDFilter implements PropertyFilter{

    private static final String filterName = "id" ;

    @Override
    public boolean filter(String column) {
        if(filterName.equals(column))
            return true ;
        return false;
    }

    @Override
    public int size() {
        return 1;
    }

}
