package com.exceptionInCompletionStagePipeline;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
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
                exceptionOccurInPipeline();
                break;
            case 2:
                exceptionallyPatternDemo();
                break;
            case 3:
                whenCompletePatternDemo();
                break;
            case 4:
                handlePatternDemo();
                break;
            case 5:
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

    /**
     * Understanding what is happen when exception is raise
     * 
     * If exception occur in CompletionStage pipeline
     * 
     * - Calling CompletableFuture.join() will throw a CompletionException
     * 
     * - Calling Future.get() will throw a ExecutionException
     */
    private static void exceptionOccurInPipeline() {
        CompletableFuture<List<User>> completableFuture = CompletableFuture.supplyAsync(remoteUserIDsService)
                .thenApply(userIds -> fetchUsersFromDB.apply(userIds));

        try {
            completableFuture.thenRun(() -> logger.log(Level.INFO, "This task will never be run"));
            completableFuture.join();
        } catch (CompletionException e) {
            logger.log(Level.SEVERE, "EXCEPTION:{0} - CAUSE:{1} - MESSAGE:{2}",
                    new Object[] { e.getClass().getName(), e.getCause().getClass().getName(), e.getMessage() });
        }

        try {
            completableFuture.thenRun(() -> logger.log(Level.INFO, "This task will never be run"));
            completableFuture.get();
        } catch (ExecutionException | InterruptedException e) {
            logger.log(Level.SEVERE, "EXCEPTION:{0} - CAUSE:{1} - MESSAGE:{2}",
                    new Object[] { e.getClass().getName(), e.getCause().getClass().getName(), e.getMessage() });
        }
    }

    /**
     * - If no exception is thrown, it return the provided value
     * 
     * - If an exception is thrown, the function is executed and the result it gives
     * is sent to the subsequent tasks
     * 
     * CompletableFuture.exceptionally() swallow an exception
     */
    private static void exceptionallyPatternDemo() {
        CompletableFuture<List<User>> completableFuture = CompletableFuture.supplyAsync(remoteUserIDsService)
                .thenApply(userIds -> fetchUsersFromDB.apply(userIds)).exceptionally(exception -> List.of());

        try {
            completableFuture.thenAccept(displayUsers).thenRun(
                    () -> logger.log(Level.INFO, "A raised exception has been caught! This task can run normally"));
            completableFuture.join();
        } catch (CompletionException e) {
            logger.log(Level.SEVERE, "EXCEPTION:{0} - CAUSE:{1} - MESSAGE:{2}",
                    new Object[] { e.getClass().getName(), e.getCause().getClass().getName(), e.getMessage() });
        }

        try {
            completableFuture.thenAccept(displayUsers).thenRun(
                    () -> logger.log(Level.INFO, "A raised exception has been caught! This task can run normally"));
            completableFuture.get();
        } catch (ExecutionException | InterruptedException e) {
            logger.log(Level.SEVERE, "EXCEPTION:{0} - CAUSE:{1} - MESSAGE:{2}",
                    new Object[] { e.getClass().getName(), e.getCause().getClass().getName(), e.getMessage() });
        }
    }

    /**
     * The CompletableFuture.whenComplete() pattern takes a result and the
     * exception, if thrown.
     * 
     * One of these two objects is null. They are passed to BiConsumer. The returned
     * completable future returns the same thing as the calling one
     * 
     * CompletableFuture.whenComplete() not swallow an exception
     */
    private static void whenCompletePatternDemo() {
        CompletableFuture.supplyAsync(remoteUserIDsService).thenApply(userIds -> fetchUsersFromDB.apply(userIds))
                .whenComplete((users, e) -> {
                    if (users != null) {
                        logger.log(Level.INFO, "No exception has been thrown");
                    } else {
                        logger.log(Level.SEVERE, "EXCEPTION:{0} - CAUSE:{1} - MESSAGE:{2}", new Object[] {
                                e.getClass().getName(), e.getCause().getClass().getName(), e.getMessage() });
                    }
                });
    }

    /**
     * The CompletableFuture.handle() pattern takes a result and the exception, if
     * thrown.
     * 
     * One of these two objects is null. They are passes to a BiFunction, that
     * produces a result. This result is returned by this completable future.
     * 
     * CompletableFuture.handle() swallow an exception
     */
    private static void handlePatternDemo() {
        CompletableFuture.supplyAsync(remoteUserIDsService).thenApply(userIds -> fetchUsersFromDB.apply(userIds))
                .handle((users, e) -> {
                    if (users != null) {
                        logger.log(Level.INFO, "No exception has been thrown");
                        return users;
                    } else {
                        logger.log(Level.SEVERE, "EXCEPTION:{0} - CAUSE:{1} - MESSAGE:{2}", new Object[] {
                                e.getClass().getName(), e.getCause().getClass().getName(), e.getMessage() });
                        return List.of();
                    }
                });
    }

    private static void summaryDemo() {
        /**
         * Not dealing for exception
         */
        CompletableFuture<List<Long>> supply = CompletableFuture.supplyAsync(remoteUserIDsService);
        CompletableFuture<List<User>> fetch = supply.thenApply(fetchUsersFromDB);
        CompletableFuture<Void> display = fetch.thenAccept(displayUsers);
        sleep(1_000);
        logger.log(Level.WARNING, "Supply : done={0} exception={1}",
                new Object[] { supply.isDone(), supply.isCompletedExceptionally() });
        logger.log(Level.WARNING, "Fetch : done={0} exception={1}",
                new Object[] { fetch.isDone(), fetch.isCompletedExceptionally() });
        logger.log(Level.WARNING, "Display : done={0} exception={1}",
                new Object[] { display.isDone(), display.isCompletedExceptionally() });

        /**
         * dealing with exception with CompletableFuture.exceptionally()
         */
        supply = CompletableFuture.supplyAsync(remoteUserIDsService);
        fetch = supply.thenApply(fetchUsersFromDB).exceptionally(exception -> List.of());
        display = fetch.thenAccept(displayUsers);
        sleep(1_000);
        logger.log(Level.WARNING, "Supply : done={0} exception={1}",
                new Object[] { supply.isDone(), supply.isCompletedExceptionally() });
        logger.log(Level.WARNING, "Fetch : done={0} exception={1}",
                new Object[] { fetch.isDone(), fetch.isCompletedExceptionally() });
        logger.log(Level.WARNING, "Display : done={0} exception={1}",
                new Object[] { display.isDone(), display.isCompletedExceptionally() });

        /**
         * dealing with exception with CompletableFuture.whenComplete()
         */
        supply = CompletableFuture.supplyAsync(remoteUserIDsService);
        fetch = supply.thenApply(fetchUsersFromDB).whenComplete((users, exception) -> {
            logger.log(Level.INFO, "do something");
        });
        display = fetch.thenAccept(displayUsers);
        sleep(1_000);
        logger.log(Level.WARNING, "Supply : done={0} exception={1}",
                new Object[] { supply.isDone(), supply.isCompletedExceptionally() });
        logger.log(Level.WARNING, "Fetch : done={0} exception={1}",
                new Object[] { fetch.isDone(), fetch.isCompletedExceptionally() });
        logger.log(Level.WARNING, "Display : done={0} exception={1}",
                new Object[] { display.isDone(), display.isCompletedExceptionally() });

        /**
         * dealing with exception with CompletableFuture.whenComplete()
         */
        supply = CompletableFuture.supplyAsync(remoteUserIDsService);
        fetch = supply.thenApply(fetchUsersFromDB).handle((users, exception) -> {
            if (users != null)
                return users;
            else
                return List.of();
        });
        display = fetch.thenAccept(displayUsers);
        sleep(1_000);
        logger.log(Level.WARNING, "Supply : done={0} exception={1}",
                new Object[] { supply.isDone(), supply.isCompletedExceptionally() });
        logger.log(Level.WARNING, "Fetch : done={0} exception={1}",
                new Object[] { fetch.isDone(), fetch.isCompletedExceptionally() });
        logger.log(Level.WARNING, "Display : done={0} exception={1}",
                new Object[] { display.isDone(), display.isCompletedExceptionally() });
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
     * Synchronous sample Data-Access which query user from database but throw
     * SQLException
     */
    private static Function<List<Long>, List<User>> fetchUsersFromDB = (List<Long> userIds) -> {
        logger.log(Level.INFO, "Running in {0}", Thread.currentThread().getName());
        logger.log(Level.INFO, "Receive userIds: {0}", userIds);
        Utils.throwException(new SQLException());
        sleep(300);
        return userIds.stream().map(userId -> new User(userId, "Sample Name", 20, "Sample City"))
                .collect(Collectors.toList());
    };

    /**
     * Asynchronous sample Data-Access which query user from database but throw
     * SQLException
     */
    private static Function<List<Long>, CompletableFuture<List<User>>> fetchUsersFromDBAsync = (List<Long> userIds) -> {
        return CompletableFuture.supplyAsync(() -> fetchUsersFromDB.apply(userIds));
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