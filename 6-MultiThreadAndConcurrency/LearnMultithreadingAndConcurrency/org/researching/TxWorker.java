package org.researching;

import java.util.logging.Level;
import java.util.logging.Logger;

public class TxWorker implements Runnable {
    public static char WITHDRAWAL = 'w';
    public static char DEPOSIT = 'd';

    protected BankAccount account;
    protected char txType;
    protected int amount;

    private static Logger logger = Logger.getLogger(TxWorker.class.getName());

    public TxWorker(BankAccount account, char txType, int amount) {
        super();
        this.account = account;
        this.txType = txType;
        this.amount = amount;
    }

    @Override
    public void run() {
        int startBalance = account.getBalance();
        if (txType == WITHDRAWAL) {
            account.withdrawal(amount);
        } else if (txType == DEPOSIT) {
            account.deposit(amount);
        }
        int endBalance = account.getBalance();
        logger.log(Level.INFO, "Start Balance: {0}, End Balance: {1}", new Object[] { startBalance, endBalance });
    }
}