package com.learning;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static Logger logger = Logger.getLogger(Main.class.getSimpleName());

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        runnableDemo(executorService);
        supplierDemo(executorService);
        createAndCompleteDemo();
        executorService.shutdown();
    }

    private static CompletableFuture<Void> runnableDemo(ExecutorService executorService) {
        return CompletableFuture.runAsync(() -> logger.log(Level.INFO,
                "Running Java app asynchronously - in thread {0}", Thread.currentThread().getName()), executorService);
    }

    private static void supplierDemo(ExecutorService executorService) {
        Supplier<String> supplier = () -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                logger.log(Level.SEVERE, "{0} - {1}", new Object[] { e.getClass().getName(), e.getMessage() });
            }
            return Thread.currentThread().getName();
        };

        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(supplier, executorService);
        logger.log(Level.INFO, "Result: {0}", completableFuture.join());
    }

    private static void createAndCompleteDemo() {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        Runnable task = () -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                logger.log(Level.SEVERE, "{0} - {1}", new Object[] { e.getClass().getName(), e.getMessage() });
            }
            completableFuture.complete(null);
        };
        CompletableFuture.runAsync(task);
        Void nil = completableFuture.join();
        logger.log(Level.INFO, "We are done");
    }
}
