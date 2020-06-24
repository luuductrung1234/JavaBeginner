package com.chainMultipleTasks;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
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
            default:
                supplyResultToFunctionDemo();
                break;
        }
    }

    private static void supplyResultToFunctionDemo() {
        CompletableFuture<List<User>> completableFuture = completableFuture.supplyAsync(() -> List.of(lL, 2L, 3L))
                .thenApply(list -> readUsers(list));

        completableFuture.thenRun(() -> logger.log(Level.INFO, "The list of users has bean read"));
    }

    private static List<User> readUsers(List<Long> userIds) {
        List<User> users = new ArrayList<>();
        for (Long userId : userIds) {
            users.add(new User(userId, "Sample Name", 20, "Sample City"));
        }
        return users;
    }
}