package oldInputOutputStream;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import data.model.Person;
import util.logging.CustomLogger;
import util.exception.ExceptionExtensions;

public class Main {
    private static Logger logger = CustomLogger.getLogger(Main.class.getSimpleName());

    public static void main(String[] args) {
        int choice = 0;
        try {
            choice = Integer.parseInt(args[0]);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Please choose a specific demo to run!");
        }

        switch (choice) {
            case 1:
                writeUsingOutputStream();
                readUsingInputStream();
                break;
            case 2:
                tryWithResource();
                break;
            default:
                tryWithResource();
                break;
        }

    }

    // ********************************************
    // * Demo Methods *****************************
    // ********************************************

    public static void writeUsingOutputStream() {
        logger.info("Use FileOutputStream");
        try (OutputStream output = new FileOutputStream("./assets/io-stream-data.txt")) {
            var data = "Learn how to:\nusing input/output stream";

            var bytes = data.getBytes(StandardCharsets.UTF_8);

            // write bytes to output stream
            output.write(bytes);

            output.flush();

        } catch (Exception e) {
            ExceptionExtensions.logMe(e, logger);
        }

        logger.info("Use ObjectOutputStream");
        try (OutputStream output = new FileOutputStream("./assets/io-stream-data.dat");
                ObjectOutputStream objectOutput = new ObjectOutputStream(output)) {
            final var people = new ArrayList<Person>();
            people.add(new Person("Steve Roger", 50));
            people.add(new Person("Robert Alan", 12));
            people.add(new Person("Jean Rey", 25));

            // write bytes to output stream
            objectOutput.writeObject(people);

            output.flush();

        } catch (Exception e) {
            ExceptionExtensions.logMe(e, logger);
        }
    }

    public static void readUsingInputStream() {
        var bytes = new byte[100];

        logger.info("Use FileInputStream");
        try (InputStream input = new FileInputStream("./assets/io-stream-data.txt")) {
            logger.log(Level.INFO, "Available bytes in the file: {0}", input.available());

            // read bytes from input stream
            var count = input.read(bytes);
            logger.log(Level.INFO, "Read bytes from file: {0}", count);

            var data = new String(bytes);
            logger.log(Level.INFO, "Content: \n{0}", data);

        } catch (Exception e) {
            ExceptionExtensions.logMe(e, logger);
        }

        logger.info("Use ByteArrayInputStream");
        try (InputStream input = new ByteArrayInputStream(bytes)) {
            logger.log(Level.INFO, "Available bytes in the file: {0}", input.available());

            var subBytes = new byte[10];
            int count = input.read(subBytes, 2, 5);
            logger.log(Level.INFO, "Read bytes from file: {0}", count);

            var data = new String(subBytes);
            logger.log(Level.INFO, "Content: \n{0}", data);

        } catch (Exception e) {
            ExceptionExtensions.logMe(e, logger);
        }

        logger.info("Use ObjectInputStream");
        try (InputStream input = new FileInputStream("./assets/io-stream-data.dat");
                ObjectInputStream objectInput = new ObjectInputStream(input)) {
            logger.log(Level.INFO, "Available bytes in the file: {0}", objectInput.available());

            var people = (ArrayList<Person>) objectInput.readObject();
            logger.log(Level.INFO, "Read bytes from file: {0}", people.size());
            for (var person : people) {
                logger.log(Level.INFO, "- {0}", person);
            }
        } catch (Exception e) {
            ExceptionExtensions.logMe(e, logger);
        }
    }

    public static void tryWithResource() {
        try (var myAutoClosable = new MyAutoCloseable()) {
            myAutoClosable.saySomething();
        } catch (Exception e) {
            ExceptionExtensions.logMe(e, logger);

            /**
             * There are multiple exception was thrown (one from saySomething(), one from
             * close()). But only an exception from saySomething() was caught. Java still
             * keep track of all exceptions, and store them as suppressed exceptions
             */
            for (Throwable t : e.getSuppressed())
                ExceptionExtensions.logMe(t, logger);
        }
    }

    /**
     * Chain streams together can create your own "high-level" streams (e.g. chain a
     * reader over a reader, etc.)
     * 
     * FilterReader, FilterWriter, FilterInputStream, FilterOutputStream:
     * <ul>
     * <li>methods call to contained stream methods</li>
     * <li>override only customized methods</li>
     * </ul>
     */
    public static void chainingStreams() {
        throw new UnsupportedOperationException();
    }
}
