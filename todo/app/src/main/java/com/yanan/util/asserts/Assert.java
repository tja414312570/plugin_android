package com.yanan.util.asserts;
/**
 *
 * assert support
 * @author yanan
 * @since plugin 2.0
 *
 */
public class Assert {
    /**
     * assert if the object is null throw NullPointerException
     * @param object assert object
     */
    public static void isNull(final Object object) {
        isNull(object, new NullPointerException());
    }
    /**
     * assert if the object is null throw NullPointerException
     * @param object assert object
     * @param message throw exception message
     */
    public static void isNull(final Object object,String message) {
        isNull(object,  new NullPointerException(message));
    }
    /**
     * assert if the object is null throw NullPointerException
     * @param object assert object
     * @param throwable custom throw exception info
     */
    public static void isNull(final Object object,RuntimeException throwable){
        isTrue(object == null,throwable);
    }
    /**
     * assert if the object is not null throw AssertNotNullException
     * @param object assert object
     * @param message custom exception message
     */
    public static void isNotNull(final Object object, String message) {
        isNotNull(object,new com.yanan.utils.asserts.AssertNotNullException(message));
    }
    /**
     * assert if the object is not null throw AssertNotNullException
     * @param object assert object
     */
    public static void isNotNull(final Object object) {
        isNotNull(object,new com.yanan.utils.asserts.AssertNotNullException());
    }
    /**
     *  assert if the object is not null throw AssertNotNullException
     * @param object assert object
     * @param throwable custom exception
     */
    public static void isNotNull(final Object object,RuntimeException throwable){
        isTrue(object != null,throwable);
    }
    /**
     *  assert if the boolean value is true throw AssertTrueException
     * @param bol assert value
     */
    public static void isTrue(final boolean bol) {
        isTrue(bol,new com.yanan.utils.asserts.AssertTrueException());
    }
    /**
     * assert if the boolean value is true throw AssertTrueException
     * @param bol assert value
     * @param message custom exception
     */
    public static void isTrue(final boolean bol, String message) {
        isTrue(bol,new com.yanan.utils.asserts.AssertTrueException(message));
    }
    /**
     * assert if the boolean value is true throw AssertTrueException
     * @param bol assert value
     * @param throwable custom exception
     */
    public static void isTrue(final boolean bol,RuntimeException throwable) {
        if(!bol) {
            throw throwable;
        }
    }
    /**
     * assert if the boolean value is false throw AssertFalseException
     * @param bol assert value
     */
    public static void isFalse(final boolean bol) {
        isFalse(bol,new com.yanan.utils.asserts.AssertFalseException());
    }
    /**
     * assert if the boolean value is false throw AssertFalseException
     * @param bol assert value
     * @param message custom message
     */
    public static void isFalse(final boolean bol, String message) {
        isFalse(bol,new com.yanan.utils.asserts.AssertFalseException(message));
    }
    /**
     * assert if the boolean value is false throw AssertFalseException
     * @param bol assert value
     * @param throwable custom exception
     */
    public static void isFalse(final boolean bol,RuntimeException throwable) {
        isTrue(!bol,throwable);
    }
    /**
     * assert the class is equals such class arrays
     * @param object ori
     * @param targets targets
     * @return is equeals one
     */
    public static boolean equalsAny(Object object, Object... targets) {
        for(Object clzz : targets) {
            if(clzz.equals(object)) {
                return true;
            }
        }
        return false;
    }

}