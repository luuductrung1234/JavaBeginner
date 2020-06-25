package com.chainMultipleTasks;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

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
                supplyResultToFunctionDemo();
                break;
            case 2:
                composingCompletableFuture();
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
        CompletableFuture<List<User>> completableFuture = CompletableFuture.supplyAsync(() -> List.of(1L, 2L, 3L))
                .thenApply(list -> fetchUsersFromDB(list));

        completableFuture.thenRun(() -> logger.log(Level.INFO, "The list of users has bean read"));
        completableFuture.thenAccept(users -> logger.log(Level.INFO, "{0} users have been read", users.size()));
    }

    /**
     * First task fetches a list of User Ids. Second task fetches Users from
     * database. Both tasks should be run asynchronously
     */
    private static void composingCompletableFuture() {
        Supplier<List<Long>> userIdSupplier = () -> remoteService();
        Function<List<Long>, List<User>> usersByIds = ids -> fetchUsersFromDB(ids);

        /**
         * this code conducted synchronously when the list of user IDs is available
         */
        CompletableFuture<List<User>> completableFuture = CompletableFuture.supplyAsync(userIdSupplier)
                .thenAccept(usersByIds);
        completableFuture.thenAccept(users -> logger.log(Level.INFO, "{0} users have been read", users.size()));

        /**
         * this code is the right way to run asynchronously
         * 
         * CompletableFuture.thenCompose() work the same concept as Stream.flatMap()
         */
        Function<List<Long>, CompletableFuture<List<User>>> usersByIdsAsync = ids -> fetchUsersFromDB(ids);
        completableFuture = CompletableFuture.supplyAsync(userIdSupplier).thenCompose(usersByIdsAsync);
        completableFuture.thenAccept(users -> logger.log(Level.INFO, "{0} users have been read", users.size()));
    }

    // ********************************************
    // * Helper Methods ***************************
    // ********************************************

    private static List<Long> remoteService() {
        return List.of(1L, 2L, 3L);
    }

    private static List<User> fetchUsersFromDB(List<Long> userIds) {
        List<User> users = new ArrayList<>();
        for (Long userId : userIds) {
            users.add(new User(userId, "Sample Name", 20, "Sample City"));
        }
        return users;
    }

}