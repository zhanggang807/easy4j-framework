package org.easy4j.framework.core.jdbc;


/**
 * <p>
 * </p>
 * User: liuyong
 * Date: 15-3-27
 * Version: 1.0
 */
public class SQL extends AbstractSQL<SQL> {

    @Override
    public SQL getSelf() {
        return this;
    }

    public static void main(String args[]){
       /* Pageable pageable = new PageRequest(0 ,20);*/
        SQL sql = new SQL();
        sql.WHERE("1!=1");
        sql.FROM("s_user");
        sql.LIMIT();
        sql.SELECT("pin");
        System.out.println("" + sql.toString());

        SQL setSql = new SQL();
        setSql.UPDATE("s_user");
        setSql.SET("pin=?").SET("x=?") ;
        setSql.WHERE("id = ?") ;
        /*TestLog.log(setSql.toString());*/
    }
}
