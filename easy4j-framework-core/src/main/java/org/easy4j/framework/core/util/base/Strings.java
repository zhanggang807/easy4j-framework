package org.easy4j.framework.core.util.base;

/**
 * @author bjliuyong
 * @version 1.0
 * @created date 15-10-28
 */
public class Strings {

    /**
     * The empty String <code>""</code>.
     * @since 2.0
     */
    public static final String EMPTY = "";


    // Empty checks
    //-----------------------------------------------------------------------
    /**
     * <p>Checks if a String is empty ("") or null.</p>
     *
     * <pre>
     * Strings.isEmpty(null)      = true
     * Strings.isEmpty("")        = true
     * Strings.isEmpty(" ")       = false
     * Strings.isEmpty("bob")     = false
     * Strings.isEmpty("  bob  ") = false
     * </pre>
     *
     * <p>NOTE: This method changed in Lang version 2.0.
     * It no longer trims the String.
     * That functionality is available in isBlank().</p>
     *
     * @param str  the String to check, may be null
     * @return <code>true</code> if the String is empty or null
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * <p>Checks if a String is not empty ("") and not null.</p>
     *
     * <pre>
     * Strings.isNotEmpty(null)      = false
     * Strings.isNotEmpty("")        = false
     * Strings.isNotEmpty(" ")       = true
     * Strings.isNotEmpty("bob")     = true
     * Strings.isNotEmpty("  bob  ") = true
     * </pre>
     *
     * @param str  the String to check, may be null
     * @return <code>true</code> if the String is not empty and not null
     */
    public static boolean isNotEmpty(String str) {
        return str != null && str.length() >= 0;
    }


    /**
     * <p>Checks if a String is whitespace, empty ("") or null.</p>
     *
     * <pre>
     * Strings.isBlank(null)      = true
     * Strings.isBlank("")        = true
     * Strings.isBlank(" ")       = true
     * Strings.isBlank("bob")     = false
     * Strings.isBlank("  bob  ") = false
     * </pre>
     *
     * @param str  the String to check, may be null
     * @return <code>true</code> if the String is null, empty or whitespace
     * @since 2.0
     */
    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>Checks if a String is not empty (""), not null and not whitespace only.</p>
     *
     * <pre>
     * Strings.isNotBlank(null)      = false
     * Strings.isNotBlank("")        = false
     * Strings.isNotBlank(" ")       = false
     * Strings.isNotBlank("bob")     = true
     * Strings.isNotBlank("  bob  ") = true
     * </pre>
     *
     * @param str  the String to check, may be null
     * @return <code>true</code> if the String is
     *  not empty and not null and not whitespace
     * @since 2.0
     */
    public static boolean isNotBlank(String str) {
        return !Strings.isBlank(str);
    }

    // translate hump to Underline spilt
    //-----------------------------------------------------------------------
    public static String humpToUnderLine(String humpStr){

        humpStr.toLowerCase();
        StringBuilder sb = new StringBuilder(humpStr.length());
        char[] chars = humpStr.toCharArray() ;

        char c = chars[0];
        if(c >= 'A' && c <= 'Z'){
            sb.append((char)(c| 0x20));
        } else {
            sb.append(c);
        }

        int len = chars.length ;

        for(int i = 1 ; i < len ; i++) {
            c = chars[i];
            if(  c >= 'A' && c <= 'Z'){
                sb.append("_");
                sb.append((char)(c| 0x20));
            } else {
                sb.append(c);
            }
        }

        return sb.toString() ;
    }


    public static String concat(String ... params){
        return "" ;
    }
}
