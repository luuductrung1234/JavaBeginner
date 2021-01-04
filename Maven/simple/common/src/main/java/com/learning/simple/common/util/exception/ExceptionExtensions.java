package com.learning.simple.common.util.exception;

import com.learning.simple.common.util.collection.ArrayExtensions;
import org.apache.logging.log4j.Logger;

public class ExceptionExtensions {
    private static final String FORMATTED_ERROR_MESSAGE = "{} - {}\n\nStack Trace:\n\n{}\n\n";

    private ExceptionExtensions() {
        super();
    }

    public static <T extends Throwable> void logMe(T exception, Logger logger) {
        if (logger.isErrorEnabled()) {
            logger.error(FORMATTED_ERROR_MESSAGE, exception.getClass().getName(), exception.getMessage(),
                    ArrayExtensions.toString(exception.getStackTrace()));
        }
    }
}
