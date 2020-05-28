package com.parallelDataProcessing;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
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
    }

    private static void parallelPerformanceDemo() {
        // generate 10M random longs in a loop
        long startTime = System.nanoTime();
        List<Long> list1 = new ArrayList<>(10_000_100);
        for (int i = 0; i < 10_000_100; i++) {
            list1.add(ThreadLocalRandom.current().nextLong());
        }
        logger.log(Level.INFO, "For-Loop - time taken: {0}", System.nanoTime() - startTime);

        // generate 10M random longs in stream
        startTime = System.nanoTime();
        Stream<Long> stream = Stream.generate(() -> {
            return ThreadLocalRandom.current().nextLong();
        });
        List<Long> list2 = stream.limit(10_000_100).collect(Collectors.toList());
        logger.log(Level.INFO, "Stream with limit() - time taken: {0} (\"Stream.limit()\" is a stateful method)",
                System.nanoTime() - startTime);

        // generate 10M random longs in stream using longs()
        startTime = System.nanoTime();
        Stream<Long> stream2 = ThreadLocalRandom.current().longs(10_000_100).mapToObj(Long::new);
        List<Long> list3 = stream2.collect(Collectors.toList());
        logger.log(Level.INFO, "Stream with longs() - time taken: {0} (\"longs()\" is a stateful method)",
                System.nanoTime() - startTime);
    }
}