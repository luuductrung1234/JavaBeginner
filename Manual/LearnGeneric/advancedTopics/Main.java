package advancedTopics;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import util.logging.CustomLogger;
import data.model.Person;

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
                typeInferenceAndLambdaExpression();
                break;
            case 2:
                intersectionTypes();
                break;
            case 3:
                intersectionTypeCast();
                break;
            case 4:
                varargAndGeneric();
                break;
            default:
                typeInferenceAndLambdaExpression();
                break;
        }

    }

    // ********************************************
    // * Demo Methods *****************************
    // ********************************************

    private static void typeInferenceAndLambdaExpression() {
        final Person steveRoger = new Person("Steve Roger", 50);
        final Person peggyOlson = new Person("Peggy Olson", 75);
        final Person bertCooper = new Person("Bert Cooper", 100);

        Predicate<Person> isOld = person -> person.getAge() > 80;

        List<Person> people = new ArrayList<>();
        people.add(steveRoger);
        people.add(peggyOlson);
        people.add(bertCooper);

        final Map<Boolean, Long> oldAndYoungPeople = people.stream()
                .collect(Collectors.partitioningBy(isOld, Collectors.counting()));
        logger.log(Level.INFO, "{0}", oldAndYoungPeople);
    }

    /**
     * Intersection Type is T extends A & B
     */
    private static void intersectionTypes() {
        Person person = PersonReader.readFromStream("./assets/person");
        logger.log(Level.INFO, "Read person from stream : {0}", person);

        person = PersonReader.readRandomFromFile("./assets/person");
        logger.log(Level.INFO, "Random read person from file : {0}", person);
    }

    /**
     * Intersection Type is T extends A & B
     */
    private static void intersectionTypeCast() {
        try {

            // intersection type cast
            // to make this object both Runnable and Serializable
            Runnable helloWorld = (Runnable & Serializable) () -> {
                System.out.println("Hello World!");
            };
            helloWorld.run();

            File file = File.createTempFile("runnable", "tmp");
            file.deleteOnExit();
            writeToFile(file, helloWorld);

            final Runnable serializedHelloWorld = (Runnable) readFromFile(file);
            if (serializedHelloWorld != null)
                serializedHelloWorld.run();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "{0} - {1}", new Object[] { e.getClass().getName(), e.getMessage() });
        }
    }

    private static void varargAndGeneric() {
        // the call arrayOf() hear is know integer type of invocation at compile time
        Integer[] integers = arrayOf(1, 2, 3, 4);
        logger.log(Level.INFO, "{0}", integers);
        logger.log(Level.INFO, "{0}", integers.getClass());

        Object[] pairOfStrings = pair("a");
        logger.log(Level.INFO, "{0}", Arrays.toString(pairOfStrings));
        logger.log(Level.INFO, "{0}", pairOfStrings.getClass());

        Object[] pairOfIntegers = pair(1);
        logger.log(Level.INFO, "{0}", Arrays.toString(pairOfIntegers));
        logger.log(Level.INFO, "{0}", pairOfIntegers.getClass());
    }

    // ********************************************
    // * Helper Methods ***************************
    // ********************************************

    /**
     * This method has a warning (Possible heap pollution from parameterized vararg
     * type)
     * 
     * Heap pollution mean that you can get a value in your heap. That's a different
     * type to the one that you compiler thinks -> lead to runtime problem
     * 
     * @param <T>
     * @param values
     * @return
     */
    private static <T> T[] arrayOf(T... values) {
        return values;
    }

    private static <T> T[] pair(T t) {
        // the call arrayOf() hear is don't know what type of invocation at compile time
        return arrayOf(t, t);
    }

    /**
     * Use @SafeVarargs to suppress the compile waring. But only use it when you can
     * convince yourself that @SafeVarargs is appropriate decision to take on the
     * simplest way of knowing that is, if you own, you read value out that list.
     * 
     * Don't assign it to a array of a different type.
     * 
     * Don't return it from the method or leak it.
     * 
     * Don't write value two different types into that list, just read value out.
     */
    @SafeVarargs
    private static <T> List<T> combine(List<T>... lists) {
        List<T> combined = new ArrayList<>();
        for (List<? extends T> list : lists) {
            combined.addAll(list);
        }
        return combined;
    }

    private static void writeToFile(final File file, Object object) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(object);
        }
    }

    private static Object readFromFile(final File file) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return ois.readObject();
        }
    }
}