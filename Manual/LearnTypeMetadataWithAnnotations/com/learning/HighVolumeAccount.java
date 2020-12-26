package com.learning;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class HighVolumeAccount extends BankAccount implements Runnable {
    private static Logger logger = Logger.getLogger(HighVolumeAccount.class.getName());

    public HighVolumeAccount(String id, int amount) {
        super(id, amount);
    }

    private int[] readDailyDeposits() {
        return new int[] { 1, 2, 3, 4 };
    }

    private int[] readDailyWithdrawals() {
        return new int[] { 1, 2, 3, 4 };
    }

    @Override
    public void run() {
        logger.log(Level.INFO, "start balance: {0}", this.getBalance());
        for (int depositAmt : readDailyDeposits()) {
            deposit(depositAmt);
        }
        for (int withdrawalAmt : readDailyWithdrawals()) {
            withdrawal(withdrawalAmt);
        }
        logger.log(Level.INFO, "end balance: {0}", this.getBalance());
    }
}