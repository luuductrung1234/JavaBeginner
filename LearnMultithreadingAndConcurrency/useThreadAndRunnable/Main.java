package useThreadAndRunnable;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        logger.log(Level.INFO, "Main thread starting");

        int choice = 0;
        try {
            choice = Integer.parseInt(args[0]);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Please choose a specific demo to run!");
        }

        switch (choice) {
            case 1:
                runMultipleThreads();
                break;
            case 2:
                determiningWhenThreadEndV1();
                break;
            case 3:
                determiningWhenThreadEndV2();
                break;
            default:
                runMultipleThreads();
                break;
        }

        logger.log(Level.INFO, "Main thread ending");
    }

    /**
     * Run multiple threads, with the main thread stay running as long as possible
     */
    public static void runMultipleThreads() {
        MyThread mt1 = MyThread.createAndStart("Child #1");
        MyThread mt2 = MyThread.createAndStart("Child #2");
        MyThread mt3 = MyThread.createAndStart("Child #3");

        for (int i = 0; i < 50; i++) {
            System.out.print(".");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                logger.log(Level.SEVERE, "Main thread interrupted. {0} - {1}",
                        new Object[] { e.getClass().getSimpleName(), e.getMessage() });
            }
        }
    }

    /**
     * Use Thread.isAlive() to determine how long Main Thread should wait for its
     * children threads
     */
    public static void determiningWhenThreadEndV1() {
        MyThread mt1 = MyThread.createAndStart("Child #1");
        MyThread mt2 = MyThread.createAndStart("Child #2");
        MyThread mt3 = MyThread.createAndStart("Child #3");

        do {
            System.out.print(".");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                logger.log(Level.SEVERE, "Main thread interrupted. {0} - {1}",
                        new Object[] { e.getClass().getSimpleName(), e.getMessage() });
            }
        } while (mt1.thread.isAlive() || mt2.thread.isAlive() || mt3.thread.isAlive());
    }

    /**
     * Use Thread.join() to determine how long Main Thread should wait for its
     * children threads
     */
    public static void determiningWhenThreadEndV2() {
        MyThread mt1 = MyThread.createAndStart("Child #1");
        MyThread mt2 = MyThread.createAndStart("Child #2");
        MyThread mt3 = MyThread.createAndStart("Child #3");

        try {
            mt1.thread.join();
            logger.log(Level.INFO, "{0} joined", mt1.thread.getName());
            mt2.thread.join();
            logger.log(Level.INFO, "{0} joined", mt2.thread.getName());
            mt3.thread.join();
            logger.log(Level.INFO, "{0} joined", mt3.thread.getName());
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "Main thread interrupted. {0} - {1}",
                    new Object[] { e.getClass().getSimpleName(), e.getMessage() });
        }
    }
}