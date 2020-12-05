package com.processingPipelinesWithOptionals;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Use Stream<T> and Optional<T> together.
 * 
 * Optional is a wrapper type.
 * 
 * Optional<T> is a special kind of Stream<T> that can have 0 or 1 element.
 * 
 * FlatMap pattern is present in both Stream api and Optional object (allow to
 * write clean and efficient code).
 */
public class Main {
    private static Logger logger = Logger.getLogger(Main.class.getSimpleName());

    public static void main(String[] args) {
        int choice = 0;
        try {
            choice = Integer.parseInt(args[0]);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Please choose a specific demo to run!");
        }

        switch (choice) {
            case 1:
                simpleDemo();
                break;
            case 2:
                flatMapDemo();
                break;
            default:
                simpleDemo();
                flatMapDemo();
                break;
        }
    }

    // ********************************************
    // * Demo Methods *****************************
    // ********************************************

    /**
     * Simple data processing with Optional<T>
     * 
     * This approach is not thread-save when a stream process in parallel (using
     * ArrayList)
     */
    private static void simpleDemo() {
        List<Double> doubles = getSampleDoubles(100);

        List<Double> result = new ArrayList<>();

        doubles.stream().forEach(d -> NewMath.sqrt(d).flatMap(NewMath::inv).ifPresent(result::add));

        logger.log(Level.INFO, "Data processing (with Optional<T>): {0}", result);
    }

    /**
     * Data processing with Optional<T> then convert to Stream<T>
     * 
     * Using Optional.flatMap() (this method is deferent from Stream.flatMap())
     * 
     * This approach is thread-save when a stream process in parallel (using
     * Collector pattern)
     */
    private static void flatMapDemo() {
        List<Double> doubles = getSampleDoubles2nd(100);

        Function<Double, Stream<Double>> invSqrtFlatMapper = d -> NewMath.inv(d) // Optional<Double>
                .flatMap(value -> NewMath.sqrt(value)) // Optional<Double>
                .map(value -> Stream.of(value)) // Optional<Stream<Double>>
                .orElseGet(() -> Stream.empty()); // Stream<Double>

        List<Double> result = doubles.stream().flatMap(invSqrtFlatMapper).collect(Collectors.toList());

        logger.log(Level.INFO, "Data processing (convert Optional<T> to Stream<T>): {0}", result);

        result = doubles.stream().parallel().flatMap(invSqrtFlatMapper).collect(Collectors.toList());

        logger.log(Level.INFO, "Parallel Data processing (convert Optional<T> to Stream<T>): {0}", result);
    }

    // ********************************************
    // * Helper Methods ***************************
    // ********************************************

    private static List<Double> getSampleDoubles(long maxSize) {
        List<Double> result = Stream.iterate(0d, doubles -> ThreadLocalRandom.current().nextDouble()).limit(maxSize)
                .collect(Collectors.toList());
        logger.log(Level.INFO, "Generate sample list of doubles: {0}", result);
        return result;
    }

    private static List<Double> getSampleDoubles2nd(long maxSize) {
        List<Double> result = ThreadLocalRandom.current().doubles(maxSize).boxed().collect(Collectors.toList());
        logger.log(Level.INFO, "Generate sample list of doubles: {0}", result);
        return result;
    }
}