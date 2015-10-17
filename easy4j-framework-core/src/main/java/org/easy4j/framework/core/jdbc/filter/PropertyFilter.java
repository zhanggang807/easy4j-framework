package org.easy4j.framework.core.jdbc.filter;

/**
 * <p>
 * </p>
 * User: liuyong
 * Date: 15-3-27
 * Version: 1.0
 */
public interface PropertyFilter {

    public static final PropertyFilter ID_FILTER = new IDFilter();

    public boolean filter(String column);

    public int size();

}
