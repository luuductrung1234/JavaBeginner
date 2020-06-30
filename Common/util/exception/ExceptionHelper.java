package util.exception;

/**
 * This Utils class help to produce unchecked Exception
 */
public class ExceptionHelper {
    protected ExceptionHelper() {
        super();
    }

    @SuppressWarnings("unchecked")
    private static <T extends Throwable> void throwException(Throwable exception, Object dummy) throws T {
        throw (T) exception;
    }

    public static void throwException(Throwable exception) {
        ExceptionHelper.<RuntimeException>throwException(exception, null);
    }
}