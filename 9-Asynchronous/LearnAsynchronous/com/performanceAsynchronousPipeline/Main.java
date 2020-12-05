package com.performanceAsynchronousPipeline;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient.Version;
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
                delayStartPatternDemo();
                break;
            case 2:
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

    private static void delayStartPatternDemo() {
        ExecutorService executor = Executors.newCachedThreadPool();

        CompletableFuture<Void> start = new CompletableFuture<>();
        start.thenCompose(nil -> getUserIds.get()).thenCompose(ids -> getUsersFromDB.apply(ids))
                .thenCompose(users -> sendEmails.apply(users));

        // at this point, a start is not running (we call that is delay)
        logger.log(Level.INFO, "At this point, a start is not running (we call that is delay)");

        // to trigger the computation, you need to complete start
        start.completeAsync(() -> null, executor);

        executor.shutdown();
    }

    private static void summaryDemo() {
        HttpClient httpClient = HttpClient.newBuilder().version(Version.HTTP_1_1).build();
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("https://www.amazon.com")).build();

        CompletableFuture<Void> start = new CompletableFuture<>();

        CompletableFuture<HttpResponse<String>> response = start
                .thenCompose(nil -> httpClient.sendAsync(request, HttpResponse.BodyHandler.asString()));

        response.thenAccept(res -> {
            String body = res.body();
            logger.log(Level.INFO, "Body = {0} [{1}]",
                    new Object[] { body.length(), Thread.currentThread().getName() });
        }).thenRun(() -> logger.log(Level.INFO, "done")).join();

        start.complete(null);
    }

    // ********************************************
    // * Helper Methods ***************************
    // ********************************************

    private static Supplier<CompletableFuture<List<Long>>> getUserIds = () -> {
        return CompletableFuture.supplyAsync(() -> Stream.of(1L, 2L, 3L).collect(Collectors.toList()));
    };

    private static Function<List<Long>, CompletableFuture<List<User>>> getUsersFromDB = (List<Long> userIds) -> {
        return CompletableFuture.supplyAsync(() -> userIds.stream()
                .map(userId -> new User(userId, "Sample Name", 20, "HCM")).collect(Collectors.toList()));
    };

    private static Function<List<User>, CompletableFuture<Void>> sendEmails = (List<User> users) -> {
        return CompletableFuture.runAsync(() -> users.stream()
                .forEach(user -> logger.log(Level.INFO, "Sending email for user {0}", user.getId())));
    };
}