package org.researching;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainConcurrency {
    private static Logger logger = Logger.getLogger(MainConcurrency.class.getName());

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        BankAccount account = new BankAccount(100);
        for (int i = 0; i < 5; i++) {
            Worker worker = new Worker(account);
            executorService.submit(worker);
        }
        try {
            executorService.shutdown();
            executorService.awaitTermination(60, TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getClass().getSimpleName() + " - " + e.getMessage(), e);
        }
    }
}