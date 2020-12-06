package oldInputOutputStream;

import java.io.IOException;
import java.util.logging.Logger;

import util.logging.CustomLogger;
import util.exception.ExceptionHelper;

/**
 * Created by Jim on 1/9/2016.
 */
public class MyAutoCloseable implements AutoCloseable {
    private static Logger logger = CustomLogger.getLogger(MyAutoCloseable.class.getSimpleName());

    public void saySomething() throws IOException {
        ExceptionHelper.throwException(new IOException("Exception from saySomething"));
        logger.info("Something");
    }

    @Override
    public void close() throws IOException {
        ExceptionHelper.throwException(new IOException("Exception from close"));
        logger.info("Close");
    }
}
