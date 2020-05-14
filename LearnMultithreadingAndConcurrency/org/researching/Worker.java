package org.researching;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.researching.BankAccount;

public class Worker implements Runnable {
    private BankAccount account;
    private static Logger logger = Logger.getLogger(Worker.class.getName());

    public Worker(BankAccount account) {
        super();
        this.account = account;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            int startBalance = account.getBalance();
            account.deposit(10);
            int endBalance = account.getBalance();
            logger.log(Level.INFO, "Start Balance: {0}, End Balance: {1}", new Object[] { startBalance, endBalance });
        }
    }

}