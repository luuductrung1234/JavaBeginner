package util.exception;

import java.util.logging.Level;
import java.util.logging.Logger;

import util.collection.ArrayExtensions;

public class ExceptionExtensions {
    private static final String FORMATTED_ERROR_MESSAGE = "{0} - {1}\n\nStack Trace:\n\n{2}\n\n";

    private ExceptionExtensions() {
        super();
    }

    public static <T extends Throwable> void logMe(T exception, Logger logger) {
        logger.log(Level.SEVERE, FORMATTED_ERROR_MESSAGE, new Object[] { exception.getClass().getName(),
                exception.getMessage(), ArrayExtensions.toString(exception.getStackTrace()) });
    }
}
