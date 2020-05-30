package com.parallelDataProcessing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
        parallelPerformanceDemo();
        sneakyStatefulOperationDemo();
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
        Person p1 = new Person("Trung", 22, "Ho Chi Minh");
        Person p2 = new Person("Yen", 23, "Vinh");
        Person p3 = new Person("Tuyen", 17, "Ho Chi Minh");

        List<Person> people = Arrays.asList(p1, p2, p3);

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