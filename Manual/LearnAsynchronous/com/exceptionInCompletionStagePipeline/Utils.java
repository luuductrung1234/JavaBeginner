package com.exceptionInCompletionStagePipeline;

/**
 * This Utils class help to produce unchecked Exception
 */
public class Utils {
    protected Utils() {
        super();
    }

    @SuppressWarnings("unchecked")
    private static <T extends Throwable> void throwException(Throwable exception, Object dummy) throws T {
        throw (T) exception;
    }

    public static void throwException(Throwable exception) {
        Utils.<RuntimeException>throwException(exception, null);
    }
}