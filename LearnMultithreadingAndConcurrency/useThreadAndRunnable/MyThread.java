package useThreadAndRunnable;

import java.util.logging.Level;
import java.util.logging.Logger;

class MyThread implements Runnable {
    private static Logger logger = Logger.getLogger(MyThread.class.getName());

    Thread thread;

    public MyThread(String name) {
        thread = new Thread(this, name);
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
            for (int count = 0; count < 10; count++) {
                Thread.sleep(400);
                logger.log(Level.INFO, "In {0}, count is {1}", new Object[] { thread.getName(), count });
            }
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "{0} interrupted. {1} - {2}",
                    new Object[] { thread.getName(), e.getClass().getSimpleName(), e.getMessage() });
        }
        logger.log(Level.INFO, "{0} terminating", thread.getName());
    }
}