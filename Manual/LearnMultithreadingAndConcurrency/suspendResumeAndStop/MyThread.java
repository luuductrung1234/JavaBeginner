package suspendResumeAndStop;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MyThread implements Runnable {
    private static Logger logger = Logger.getLogger(MyThread.class.getName());

    Thread thread;
    boolean suspended;
    boolean stopped;

    public MyThread(String name) {
        thread = new Thread(this, name);
        suspended = false;
        stopped = false;
    }

    public static MyThread createAndStart(String name) {
        MyThread myThread = new MyThread(name);
        myThread.thread.start();
        return myThread;
    }

    @Override
    public void run() {
        logger.log(Level.INFO, "{0} starting", thread.getName());

        try {
            for (int i = 0; i < 1000; i++) {
                // doing something
                System.out.print(i + " ");
                if ((i % 10) == 0) {
                    System.out.println();
                    Thread.sleep(250);
                }

                // use synchronized block to check suspended and stopped
                synchronized (this) {
                    while (suspended) {
                        wait();
                    }
                    if (stopped)
                        break;
                }
            }
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "{0} interrupted. {1} - {2}",
                    new Object[] { thread.getName(), e.getClass().getSimpleName(), e.getMessage() });
        }

        logger.log(Level.INFO, "{0} existing", thread.getName());
    }

    /**
     * Stop the thread
     */
    synchronized void myStop() {
        stopped = true;

        // the following ensures that a suspended thread can be stopped.
        suspended = false;
        notify();

        System.out.println(thread.getName() + " is Stopped.");
    }

    /**
     * Suspend the thread
     */
    synchronized void mySuspend() {
        suspended = true;

        System.out.println(thread.getName() + " is Suspended.");
    }

    /**
     * Resume the thread
     */
    synchronized void myResume() {
        suspended = false;
        notify();

        System.out.println(thread.getName() + " is Resumed.");
    }
}