package org.easy4j.framework.core.orm;

/**
 * @author: liuyong
 * @since 1.0
 */
public class DbAccessException extends RuntimeException {

    private String SQLState ;

    /*private String vendorCode ;*/

    public DbAccessException(String reason){
        super(reason);
    }

    public DbAccessException(Throwable cause){
        super(cause);
    }

    public DbAccessException(String reason ,Throwable cause){
        super(reason,cause);
    }

    public DbAccessException(String reason, String sqlState, Throwable cause) {
        super(reason,cause);
        this.SQLState = sqlState;
    }

   /* public DbAccessException(String reason, String sqlState, String  vendorCode) {
        super(reason,cause);
        this.SQLState = sqlState;
    }*/

    public String getSQLState() {
        return SQLState;
    }

    /*public String getVendorCode() {
        return vendorCode;
    }*/
}
