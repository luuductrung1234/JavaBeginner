package suspendResumeAndStop;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        logger.log(Level.INFO, "Main thread starting");

        MyThread thread1 = MyThread.createAndStart("MyThread #1");

        try {
            mainThreadDoJob(1000);

            thread1.mySuspend();
            mainThreadDoJob(1000);

            thread1.myResume();
            mainThreadDoJob(1000);

            thread1.mySuspend();
            mainThreadDoJob(1000);

            thread1.myResume();
            mainThreadDoJob(1000);

            thread1.myStop();
            mainThreadDoJob(1000);

        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "Main thread interrupted. {0} - {1}",
                    new Object[] { e.getClass().getSimpleName(), e.getMessage() });
        }

        // wait for thread to finish
        try {
            thread1.thread.join();
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "Main thread interrupted. {0} - {1}",
                    new Object[] { e.getClass().getSimpleName(), e.getMessage() });
        }

        logger.log(Level.INFO, "Main thread ending");
    }

    private static void mainThreadDoJob(long millis) throws InterruptedException {
        Thread.sleep(millis);
    }
}