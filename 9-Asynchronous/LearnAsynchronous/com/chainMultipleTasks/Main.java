package com.chainMultipleTasks;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
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
            case 4:
                summaryDemo();
                break;
            default:
                summaryDemo();
                break;
        }
    }

    // ********************************************
    // * Demo Methods *****************************
    // ********************************************

    private static void supplyResultToFunctionDemo() {
        CompletableFuture<List<User>> completableFuture = CompletableFuture
                .supplyAsync(() -> Stream.of(1L, 2L, 3L).collect(Collectors.toList())).thenApply(fetchUsersFromDB);

        completableFuture.thenRun(() -> logger.log(Level.INFO, "The list of users has bean read"));
        completableFuture.thenAccept(displayUsers);
        completableFuture.join();
    }

    /**
     * Compose asynchronous tasks
     */
    private static void composingCompletableFuture() {
        /**
         * remoteUserIDsService() is a synchronous task, it conducted synchronously when
         * the list of user IDs is available
         */
        logger.log(Level.INFO, "CHAIN TWO SYNCHRONOUS TASKS");
        CompletableFuture<List<User>> completableFuture = CompletableFuture.supplyAsync(remoteUserIDsService)
                .thenApply(fetchUsersFromDB);
        completableFuture.thenAccept(displayUsers);
        completableFuture.join();

        /**
         * fetchUsersFromDBAsync() is a asynchronous task, it will return
         * CompletableFuture<>
         * 
         * to chain fetchUsersFromDBAsync() after remoteUserIDsService(), we have to
         * compose them
         * 
         * CompletableFuture.thenCompose() work the same concept as Stream.flatMap()
         */
        logger.log(Level.INFO, "COMPOSE ASYNCHRONOUS TASK");
        completableFuture = CompletableFuture.supplyAsync(remoteUserIDsService).thenCompose(fetchUsersFromDBAsync);
        completableFuture.thenAccept(displayUsers);
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
            return fetchUsersFromDB.apply(list);
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
            return fetchUsersFromDB.apply(list);
        });
        cf2.join();

        logger.log(Level.WARNING, "BE CAREFUL: moving data from one thread to another is costly");

        /**
         * Two task are executed in different threads of given pool
         */
        logger.log(Level.INFO, "TWO TASK IN DIFFERENT THREAD OF GIVEN POOL");
        CompletableFuture<List<User>> cf3 = CompletableFuture.supplyAsync(() -> {
            logger.log(Level.INFO, "Supplier run thread: {0}", Thread.currentThread().getName());
            return Stream.of(1L, 2L, 3L).collect(Collectors.toList());
        }, executorService).thenApplyAsync((list) -> {
            logger.log(Level.INFO, "Function run thread: {0}", Thread.currentThread().getName());
            return fetchUsersFromDB.apply(list);
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
            return fetchUsersFromDB.apply(list);
        });
        cf4.join();

        executorService.shutdown();
    }

    /**
     * Summary Demo (contains all concepts above)
     */
    private static void summaryDemo() {
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        ExecutorService multiThreadsExecutor = Executors.newFixedThreadPool(5);

        /**
         * Simple chain of tasks
         */
        logger.log(Level.INFO, "SIMPLE CHAIN OF TASKS");
        CompletableFuture.supplyAsync(remoteUserIDsService).thenApplyAsync(fetchUsersFromDB, singleThreadExecutor)
                .thenAcceptAsync(displayUsers, singleThreadExecutor);

        CompletableFuture<List<Long>> userIdsFuture = CompletableFuture.supplyAsync(remoteUserIDsService);

        /**
         * Trigger task after two previous tasks done
         */
        logger.log(Level.INFO, "TRIGGER TASK AFTER TWO PREVIOUS TASKS DONE");
        CompletableFuture<List<User>> usersFuture = userIdsFuture.thenComposeAsync(fetchUsersFromDBAsync,
                multiThreadsExecutor);
        CompletableFuture<List<String>> emailsFuture = userIdsFuture.thenComposeAsync(fetchEmailsFromDBAsync,
                multiThreadsExecutor);
        usersFuture.thenAcceptBoth(emailsFuture, (users, emails) -> {
            logger.log(Level.INFO, "Fetched #{0} users and #{1} emails", new Object[] { users.size(), emails.size() });
        });

        /**
         * Trigger task after either one of previous tasks done
         */
        logger.log(Level.INFO, "TRIGGER TASK AFTER EITHER ONE OF PREVIOUS TASKS DONE");
        CompletableFuture<List<User>> usersFuture1st = userIdsFuture.thenComposeAsync(fetchUsersFromDBAsync,
                multiThreadsExecutor);
        CompletableFuture<List<User>> usersFuture2nd = userIdsFuture.thenComposeAsync(fetchUsersFromDBAsync,
                multiThreadsExecutor);
        usersFuture1st.thenRun(() -> logger.log(Level.INFO, "1st complete"));
        usersFuture2nd.thenRun(() -> logger.log(Level.INFO, "2nd complete"));
        usersFuture1st.acceptEither(usersFuture2nd, displayUsers);

        sleep(6_000);
        singleThreadExecutor.shutdown();
        multiThreadsExecutor.shutdown();
    }

    // ********************************************
    // * Helper Methods ***************************
    // ********************************************

    /**
     * Sample RemoteService which is run some where in internet
     */
    private static Supplier<List<Long>> remoteUserIDsService = () -> {
        sleep(200);
        return Stream.of(1L, 2L, 3L).collect(Collectors.toList());
    };

    /**
     * Synchronous sample Data-Access which query user from database
     */
    private static Function<List<Long>, List<User>> fetchUsersFromDB = (List<Long> userIds) -> {
        logger.log(Level.INFO, "Running in {0}", Thread.currentThread().getName());
        logger.log(Level.INFO, "Receive userIds: {0}", userIds);
        sleep(300);
        return userIds.stream().map(userId -> new User(userId, "Sample Name", 20, "Sample City"))
                .collect(Collectors.toList());
    };

    /**
     * Synchronous sample Data-Access which query user's email from database
     */
    private static Function<List<Long>, List<String>> fetchEmailsFromDB = (List<Long> userIds) -> {
        logger.log(Level.INFO, "Running in {0}", Thread.currentThread().getName());
        logger.log(Level.INFO, "Receive userIds: {0}", userIds);
        sleep(300);
        return userIds.stream().map(userId -> "example_user" + userId + "@email.com").collect(Collectors.toList());
    };

    /**
     * Asynchronous sample Data-Access which query user from database
     */
    private static Function<List<Long>, CompletableFuture<List<User>>> fetchUsersFromDBAsync = (List<Long> userIds) -> {
        return CompletableFuture.supplyAsync(() -> fetchUsersFromDB.apply(userIds));
    };

    /**
     * Asynchronous sample Data-Access which query user's email from database
     */
    private static Function<List<Long>, CompletableFuture<List<String>>> fetchEmailsFromDBAsync = (
            List<Long> userIds) -> {
        return CompletableFuture.supplyAsync(() -> fetchEmailsFromDB.apply(userIds));
    };

    private static Consumer<List<User>> displayUsers = users -> {
        logger.log(Level.INFO, "*********** DISPLAY USERS ***********");
        logger.log(Level.INFO, "{0} users have been read.", users.size());
        users.forEach(user -> logger.log(Level.INFO, " - {0}", user));
        logger.log(Level.INFO, "*************************************");
    };

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "{0} - {1}", new Object[] { e.getClass().getName(), e.getMessage() });
        }
    }
}