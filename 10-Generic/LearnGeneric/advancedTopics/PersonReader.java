package advancedTopics;

import java.io.Closeable;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import util.exception.ExceptionHelper;
import util.logging.CustomLogger;

public class PersonReader {
    private static Logger logger = CustomLogger.getLogger(PersonReader.class.getSimpleName());

    /**
     * this method is an example for intersection type, T extends from both
     * DataInput and Closeable
     * 
     * @param <T>
     * @param source
     * @return
     */
    public <T extends DataInput & Closeable> Person read(final T source) {
        try (T input = source) {
            return new Person(input.readUTF(), input.readInt());
        } catch (IOException e) {
            ExceptionHelper.throwException(e);
        }
        return null;
    }

    public static Person readFromStream(String path) {
        PersonReader reader = new PersonReader();

        try (DataInputStream inputStream = new DataInputStream(Files.newInputStream(Paths.get(path)))) {
            return reader.read(inputStream);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "{0} - {1}", new Object[] { e.getClass().getName(), e.getMessage() });
        }
        return null;
    }

    public static Person readRandomFromFile(String path) {
        PersonReader reader = new PersonReader();

        try (RandomAccessFile randomAccessFile = new RandomAccessFile(path, "rw")) {
            return reader.read(randomAccessFile);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "{0} - {1}", new Object[] { e.getClass().getName(), e.getMessage() });
        }
        return null;
    }
}