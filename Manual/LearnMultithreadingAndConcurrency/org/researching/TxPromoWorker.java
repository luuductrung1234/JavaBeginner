package org.researching;

import java.util.logging.Level;
import java.util.logging.Logger;

public class TxPromoWorker extends TxWorker {
    private static Logger logger = Logger.getLogger(TxPromoWorker.class.getName());

    public TxPromoWorker(BankAccount account, char txType, int amount) {
        super(account, txType, amount);
    }

    @Override
    public void run() {
        int startBalance = account.getBalance();
        if (txType == WITHDRAWAL) {
            account.withdrawal(amount);
        } else if (txType == DEPOSIT) {
            synchronized (account) {
                account.deposit(amount);
                if (account.getBalance() > 500) {
                    // bonus business logic
                    int bonus = (int) ((account.getBalance() - 500) * 0.1);
                    account.deposit(bonus);
                }
            }
        }
        int endBalance = account.getBalance();
        logger.log(Level.INFO, "Start Balance: {0}, End Balance: {1}", new Object[] { startBalance, endBalance });
    }
}