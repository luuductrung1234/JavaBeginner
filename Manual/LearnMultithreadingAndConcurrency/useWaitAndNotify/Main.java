package useWaitAndNotify;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        logger.log(Level.INFO, "Main thread starting");

        TickTock tickTock = new TickTock();
        MyThread thread1 = MyThread.createAndStart("Tick", tickTock);
        MyThread thread2 = MyThread.createAndStart("Tock", tickTock);

        try {
            thread1.thread.join();
            thread2.thread.join();
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "Main thread interrupted. {0} - {1}",
                    new Object[] { e.getClass().getSimpleName(), e.getMessage() });
        }

        logger.log(Level.INFO, "Main thread ending");
    }
}