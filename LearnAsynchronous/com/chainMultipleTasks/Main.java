package com.chainMultipleTasks;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import util.logging.CustomLogger;

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
                supplyResultToFunctionDemo();
                break;
            case 2:
                composingCompletableFuture();
                break;
            case 3:
                threadToRunTriggeredTask();
                break;
            default:
                supplyResultToFunctionDemo();
                break;
        }
    }

    // ********************************************
    // * Demo Methods *****************************
    // ********************************************

    private static void supplyResultToFunctionDemo() {
        CompletableFuture<List<User>> completableFuture = CompletableFuture
                .supplyAsync(() -> Stream.of(1L, 2L, 3L).collect(Collectors.toList()))
                .thenApply(list -> fetchUsersFromDB(list));

        completableFuture.thenRun(() -> logger.log(Level.INFO, "The list of users has bean read"));
        completableFuture.thenAccept(users -> logger.log(Level.INFO, "{0} users have been read", users.size()));
        completableFuture.join();
    }

    /**
     * First task fetches a list of User Ids. Second task fetches Users from
     * database. Both tasks should be run asynchronously
     */
    private static void composingCompletableFuture() {
        Supplier<List<Long>> getUserIds = () -> remoteService();
        Function<List<Long>, List<User>> getUsersByIds = ids -> fetchUsersFromDB(ids);

        /**
         * getUsersByIds() is a synchronous function, it conducted synchronously when
         * the list of user IDs is available
         */
        logger.log(Level.INFO, "RUN SYNCHRONOUS CHAIN OF TASKS");
        CompletableFuture<List<User>> completableFuture = CompletableFuture.supplyAsync(getUserIds)
                .thenApply(getUsersByIds);
        completableFuture.thenAccept(users -> logger.log(Level.INFO, "{0} users have been read", users.size()));
        completableFuture.thenRun(() -> logger.log(Level.INFO, "The list of users has bean read"));
        completableFuture.join();

        /**
         * getUsersByIdsAsync() is a asynchronous function, it will return
         * CompletableFuture<>
         * 
         * CompletableFuture.thenCompose() work the same concept as Stream.flatMap()
         */
        logger.log(Level.INFO, "RUN ASYNCHRONOUS CHAIN OF TASKS");
        Function<List<Long>, CompletableFuture<List<User>>> getUsersByIdsAsync = ids -> fetchUsersFromDBAsync(ids);
        completableFuture = CompletableFuture.supplyAsync(getUserIds).thenCompose(getUsersByIdsAsync);
        completableFuture.thenAccept(users -> logger.log(Level.INFO, "{0} users have been read", users.size()));
        completableFuture.join();
    }

    /**
     * Which thread a triggered task is executed
     */
    private static void threadToRunTriggeredTask() {
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        /**
         * Two task are executed in the same thread of given pool
         */
        logger.log(Level.INFO, "TWO TASK IN SAME THREAD OF GIVEN POOL");
        CompletableFuture<List<User>> cf1 = CompletableFuture.supplyAsync(() -> {
            logger.log(Level.INFO, "Supplier run thread: {0}", Thread.currentThread().getName());
            return Stream.of(1L, 2L, 3L).collect(Collectors.toList());
        }, executorService).thenApply((list) -> {
            logger.log(Level.INFO, "Function run thread: {0}", Thread.currentThread().getName());
            return fetchUsersFromDB(list);
        });
        cf1.join();

        /**
         * Two task are executed in the same thread of Common Fork/Join pool
         */
        logger.log(Level.INFO, "TWO TASK IN SAME THREAD OF COMMON FORK/JOIN POOL");
        CompletableFuture<List<User>> cf2 = CompletableFuture.supplyAsync(() -> {
            logger.log(Level.INFO, "Supplier run thread: {0}", Thread.currentThread().getName());
            return Stream.of(1L, 2L, 3L).collect(Collectors.toList());
        }).thenApply((list) -> {
            logger.log(Level.INFO, "Function run thread: {0}", Thread.currentThread().getName());
            return fetchUsersFromDB(list);
        });
        cf2.join();

        /**
         * Two task are executed in different threads of given pool
         */
        logger.log(Level.INFO, "TWO TASK IN DIFFERENT THREAD OF GIVEN POOL");
        CompletableFuture<List<User>> cf3 = CompletableFuture.supplyAsync(() -> {
            logger.log(Level.INFO, "Supplier run thread: {0}", Thread.currentThread().getName());
            return Stream.of(1L, 2L, 3L).collect(Collectors.toList());
        }, executorService).thenApplyAsync((list) -> {
            logger.log(Level.INFO, "Function run thread: {0}", Thread.currentThread().getName());
            return fetchUsersFromDB(list);
        }, executorService);
        cf3.join();

        /**
         * Two task are executed in different threads of Common Fork/Join pool
         */
        logger.log(Level.INFO, "TWO TASK IN DIFFERENT THREAD OF COMMON FORK/JOIN POOL");
        CompletableFuture<List<User>> cf4 = CompletableFuture.supplyAsync(() -> {
            logger.log(Level.INFO, "Supplier run thread: {0}", Thread.currentThread().getName());
            return Stream.of(1L, 2L, 3L).collect(Collectors.toList());
        }).thenApplyAsync((list) -> {
            logger.log(Level.INFO, "Function run thread: {0}", Thread.currentThread().getName());
            return fetchUsersFromDB(list);
        });
        cf4.join();

        executorService.shutdown();
    }

    // ********************************************
    // * Helper Methods ***************************
    // ********************************************

    private static List<Long> remoteService() {
        return Stream.of(1L, 2L, 3L).collect(Collectors.toList());
    }

    private static List<User> fetchUsersFromDB(List<Long> userIds) {
        logger.log(Level.INFO, "receive userIds: {0}", userIds);

        List<User> users = new ArrayList<>();
        for (Long userId : userIds) {
            users.add(new User(userId, "Sample Name", 20, "Sample City"));
        }
        return users;
    }

    private static CompletableFuture<List<User>> fetchUsersFromDBAsync(List<Long> userIds) {
        return CompletableFuture.supplyAsync(() -> {
            logger.log(Level.INFO, "receive userIds: {0}", userIds);

            List<User> users = new ArrayList<>();
            for (Long userId : userIds) {
                users.add(new User(userId, "Sample Name", 20, "Sample City"));
            }
            return users;
        });
    }

}