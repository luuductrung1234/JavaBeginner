package com.parallelDataProcessing;

import java.util.logging.Level;
import java.util.logging.Logger;

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
    }
}