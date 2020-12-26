package com.learning;

import java.lang.reflect.Constructor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static Logger logger = Logger.getLogger(Main.class.getSimpleName());

    public static void main(String[] args) {
        try {
            BankAccount account = new BankAccount("1010", 400);
            startWork("com.learning.AccountWorker", account);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "{0} - {1}", new Object[] { e.getClass().getSimpleName(), e.getMessage() });
        }
    }

    private static void startWorkSelfContained(Object workerTarget) throws Exception {
        Class<?> targetType = workerTarget.getClass();
        ProcessedBy processedBy = targetType.getAnnotation(ProcessedBy.class);
        Class<?> workerType = processedBy.value();
        startWork(workerType, workerTarget);
    }

    private static void startWork(String workerTypeName, Object workerTarget) throws Exception {
        Class<?> workerType = Class.forName(workerTypeName);
        startWork(workerType, workerTarget);
    }

    private static void startWork(Class<?> workerType, Object workerTarget) throws Exception {
        Constructor<?> workerConstructor = workerType.getDeclaredConstructor();
        TaskWorker worker = (TaskWorker) workerConstructor.newInstance();

        worker.setTarget(workerTarget);
        WorkHandler workHandler = workerType.getAnnotation(WorkHandler.class);
        if (workHandler == null)
            throw new Exception();
        ExecutorService pool = Executors.newFixedThreadPool(5);
        if (workHandler.useThreadPool()) {
            pool.submit(new Runnable() {
                public void run() {
                    worker.doWork();
                }
            });
        } else {
            worker.doWork();
        }
    }
}
