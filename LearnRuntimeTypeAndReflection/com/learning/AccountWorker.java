package com.learning;

import static java.lang.System.out;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AccountWorker implements Runnable, TaskWorker {
    private static Logger logger = Logger.getLogger(AccountWorker.class.getName());

    private BankAccount bankAccount;

    public void setTarget(Object target) {
        if (BankAccount.class.isInstance(target))
            bankAccount = (BankAccount) target;
        else
            throw new IllegalArgumentException(
                    "Target object is not an instance of " + BankAccount.class.getSimpleName());
    }

    public void doWork() {
        Thread thread = new Thread(
                HighVolumeAccount.class.isInstance(bankAccount) ? (HighVolumeAccount) bankAccount : this);
        thread.start();
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        out.print("Input txType: ");
        char txType = scanner.next().charAt(0);
        out.print("Input amount: ");
        int amount = scanner.nextInt();

        logger.log(Level.INFO, "start balance: {0}", bankAccount.getBalance());
        if (txType == 'w') {
            bankAccount.withdrawal(amount);
        } else {
            bankAccount.deposit(amount);
        }
        logger.log(Level.INFO, "end balance: {0}", bankAccount.getBalance());
    }
}