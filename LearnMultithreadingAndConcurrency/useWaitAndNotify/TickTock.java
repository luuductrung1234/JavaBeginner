package useWaitAndNotify;

import java.util.logging.Level;
import java.util.logging.Logger;

public class TickTock {
    private static Logger logger = Logger.getLogger(Main.class.getName());

    String state; // contains the state of the clock

    synchronized void tick(boolean running) {
        state = "ticked";

        if (!running) {
            // stop the clock
            notify(); // notify any waiting threads
            return;
        }

        logger.log(Level.INFO, "Tick ");

        notify(); // let tock() run

        try {
            while (!state.equals("tocked"))
                wait(); // wait for tock() to complete
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "Thread interrupted. {0} - {1}",
                    new Object[] { e.getClass().getSimpleName(), e.getMessage() });
        }
    }

    synchronized void tock(boolean running) {
        state = "tocked";

        if (!running) {
            // stop the clock
            notify(); // notify any waiting threads
            return;
        }

        logger.log(Level.INFO, "Tock ");

        notify(); // let tick() run

        try {
            while (!state.equals("ticked"))
                wait(); // wait for tick() to complete
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "Thread interrupted. {0} - {1}",
                    new Object[] { e.getClass().getSimpleName(), e.getMessage() });
        }
    }
}