package com.completionStage;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A task is a runnable or supplier object
 * 
 * CompletableFuture is implement both Future and CompletionStage interfaces
 * 
 * CompletionStage allow to chains multiple tasks
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

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        switch (choice) {
            case 1:
                runnableDemo(executorService);
                break;
            case 2:
                supplierDemo(executorService);
                break;
            case 3:
                forceTaskToComplete(executorService);
                break;
            case 4:
                createAndCompleteDemo();
                break;
            default:
                createAndCompleteDemo();
                break;
        }

        // remember to shutdown a Executor
        executorService.shutdown();
    }

    /**
     * Run task is a Runnable
     * 
     * @param executorService
     */
    private static CompletableFuture<Void> runnableDemo(ExecutorService executorService) {
        Runnable task = () -> logger.log(Level.INFO, "Running Java app asynchronously - in thread {0}",
                Thread.currentThread().getName());
        return CompletableFuture.runAsync(task, executorService);
    }

    /**
     * Run task is a Supplier
     * 
     * @param executorService
     */
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

    /**
     * using CompletableFuture.Complete() & CompletableFuture.ObtrudeValue() to
     * force task complete
     * 
     * @param executorService
     */
    private static void forceTaskToComplete(ExecutorService executorService) {
        Supplier<String> supplier = () -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                logger.log(Level.SEVERE, "{0} - {1}", new Object[] { e.getClass().getName(), e.getMessage() });
            }
            return Thread.currentThread().getName();
        };

        /**
         * call complete() before task is done
         */
        logger.log(Level.INFO, "call complete() before task is done");
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(supplier, executorService);
        completableFuture.complete("Complete now! It take to long.");
        String result = completableFuture.join();
        logger.log(Level.INFO, "Result: {0}", result);

        /**
         * call complete() after task is done
         */
        logger.log(Level.INFO, "call complete() after task is done");
        completableFuture = CompletableFuture.supplyAsync(supplier, executorService);
        result = completableFuture.join();
        logger.log(Level.INFO, "Result: {0}", result);
        completableFuture.complete("Complete now! It take to long.");
        result = completableFuture.join();
        logger.log(Level.INFO, "Result: {0}", result);

        /**
         * call obtrudeValue() before task is done
         */
        logger.log(Level.INFO, "call obtrudeValue() before task is done");
        completableFuture = CompletableFuture.supplyAsync(supplier, executorService);
        completableFuture.obtrudeValue("Complete now! It take to long.");
        result = completableFuture.join();
        logger.log(Level.INFO, "Result: {0}", result);
        result = completableFuture.join();
        logger.log(Level.INFO, "Result: {0}", result);

        /**
         * call obtrudeValue() after task is done
         */
        logger.log(Level.INFO, "call obtrudeValue() after task is done");
        completableFuture = CompletableFuture.supplyAsync(supplier, executorService);
        result = completableFuture.join();
        logger.log(Level.INFO, "Result: {0}", result);
        completableFuture.obtrudeValue("Complete now! It take to long.");
        result = completableFuture.join();
        logger.log(Level.INFO, "Result: {0}", result);
    }

    private static void createAndCompleteDemo() {
        // manual created CompletableFuture object
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
        logger.log(Level.INFO, "We are done.");
    }
}
