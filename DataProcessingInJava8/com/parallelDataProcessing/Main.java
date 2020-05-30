package com.parallelDataProcessing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalDouble;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * - Multi-thread: one process = one thread, so many processes at the same time.
 * (problem: race condition, thread synchronization, variable visibility)
 * 
 * - Parallel: one process = many thread, to go faster (problem: algorithm, data
 * distribution among the CPU cores and the balancing of the CPU loads)
 * 
 * 
 * Concurrency is about dealing with lots of things at once. Parallelism is
 * about doing lots of things at once. Parallelism is applied to processing data
 * 
 * Example: Think about quick sorting a set of integers. If we in multi-thread
 * environment, each thread sort one given set of integers. If we in parallel
 * environment,it means that a pool of thread will be able to sort one set of
 * integers
 * 
 * Reference:
 * https://howtodoinjava.com/java/multi-threading/concurrency-vs-parallelism/
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
                parallelPerformanceDemo();
                break;
            case 2:
                sneakyStatefulOperationDemo();
                break;
            case 3:
                parallelReductionDemo();
                break;
            case 4:
                tuningParallelismDemo();
                break;
            default:
                parallelPerformanceDemo();
                sneakyStatefulOperationDemo();
                parallelReductionDemo();
                tuningParallelismDemo();
                break;
        }
    }

    /**
     * Stateful operation in Parallel Data Processing
     */
    private static void parallelPerformanceDemo() {
        runWithPerformanceLogging("For-Loop", () -> {
            // generate 10M random longs in a loop
            List<Long> list1 = new ArrayList<>(10_000_100);
            for (int i = 0; i < 10_000_100; i++) {
                list1.add(ThreadLocalRandom.current().nextLong());
            }
            return null;
        });

        runWithPerformanceLogging("Stream with limit()", () -> {
            // generate 10M random longs in stream
            // limit() is a stateful operation
            Stream<Long> stream = Stream.generate(() -> {
                return ThreadLocalRandom.current().nextLong();
            });
            List<Long> list2 = stream.limit(10_000_100).collect(Collectors.toList());
            return null;
        });

        runWithPerformanceLogging("Stream with longs()", () -> {
            // generate 10M random longs in stream using longs()
            // longs() is a stateful operation
            Stream<Long> stream2 = ThreadLocalRandom.current().longs(10_000_100).mapToObj(Long::valueOf);
            List<Long> list3 = stream2.collect(Collectors.toList());
            return null;
        });
    }

    /**
     * Hidden stateful operation - Demo
     */
    private static void sneakyStatefulOperationDemo() {
        List<Person> people = getListOfPeople(10);

        runWithPerformanceLogging("Sneaky Stateful Operation", people.stream(), (stream) -> {
            // List is a ordered data structure, so that a stream of List is ordered.
            // That make this operation sneaky stateful
            stream.parallel().filter(person -> person.getAge() > 20)
                    .forEach(person -> logger.log(Level.INFO, person.toString()));
        });

        runWithPerformanceLogging("Fix Sneaky Stateful Operation", people.stream(), (stream) -> {
            // unordered() make an operation ignore a ordered constraint of List
            // That make this operation stateless
            stream.parallel().unordered().filter(person -> person.getAge() > 20)
                    .forEach(person -> logger.log(Level.INFO, person.toString()));
        });
    }

    /**
     * Map / Filter / Reduce in parallel
     */
    private static void parallelReductionDemo() {
        List<Person> people = getListOfPeople(10);

        runWithPerformanceLogging("ArrayList reduce in parallel", () -> {
            // ArrayList is not concurrent aware, and race conditions will occur
            List<Integer> ages = people.stream().parallel().reduce(new ArrayList<Integer>(), (list, p) -> {
                list.add(p.getAge());
                return list;
            }, (list1, list2) -> list1);
            logger.log(Level.INFO, "Reduce list of people to list of ages - in parallel with ArrayList: {0}", ages);
            return null;
        });

        runWithPerformanceLogging("ArrayList reduce in parallel using Collector Pattern", () -> {
            // Collectors.toList() will handle parallelism and thread-safety
            List<Integer> ages = people.stream().parallel().map(Person::getAge).collect(Collectors.toList());
            logger.log(Level.INFO, "Reduce list of people to list of ages - in parallel using Collector Pattern: {0}",
                    ages);
            return null;
        });
    }

    /**
     * Tuning Parallelism
     */
    private static void tuningParallelismDemo() {
        try {
            List<Person> people = getListOfPeople(10);

            ForkJoinPool forkJoinPool = new ForkJoinPool(2);
            Future<OptionalDouble> futureResult = forkJoinPool.submit(() -> {
                return people.stream().parallel().mapToInt(p -> p.getAge()).filter(age -> age > 20).average();
            });
            logger.log(Level.INFO, "Average ages (that bigger than 20): {0}", futureResult.get());
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getClass().getSimpleName() + " - " + e.getMessage(), e);
        }
    }

    /**
     * Helper method to get a sample list of people
     * 
     * @return
     */
    private static List<Person> getListOfPeople(int count) {
        List<String> cities = Arrays.asList("Ho Chi Minh", "Vinh", "Ben Tre", "Quang Binh");
        List<Person> people = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            StringBuilder nameBuilder = new StringBuilder();
            nameBuilder.append("User ").append(i);
            int randomAge = ThreadLocalRandom.current().nextInt(10, 100 + 1);
            int randomCity = ThreadLocalRandom.current().nextInt(0, 3 + 1);
            people.add(new Person(nameBuilder.toString(), randomAge, cities.get(randomCity)));
        }
        logger.log(Level.INFO, "Generated sample people: {0}", people);
        return people;
    }

    /**
     * Run a supplier operation with performance analyst
     * 
     * @param operationName
     * @param operation
     */
    private static void runWithPerformanceLogging(String operationName, Supplier<Void> operation) {
        long startTime = System.nanoTime();
        operation.get();
        logger.log(Level.INFO, "{0} - time taken: {1}", new Object[] { operationName, System.nanoTime() - startTime });
    }

    /**
     * Run a consumer operation with performance analyst
     * 
     * @param <T>
     * @param operationName
     * @param dataStream
     * @param operation
     */
    private static <T> void runWithPerformanceLogging(String operationName, Stream<T> dataStream,
            Consumer<Stream<T>> operation) {
        long startTime = System.nanoTime();
        operation.accept(dataStream);
        logger.log(Level.INFO, "{0} - time taken: {1}", new Object[] { operationName, System.nanoTime() - startTime });
    }
}