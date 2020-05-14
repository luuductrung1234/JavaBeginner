package org.researching;

public class BankAccount {
    private int balance;

    public BankAccount(int startBalance) {
        super();
        balance = startBalance;
    }

    /**
     * @return the balance
     */
    public int getBalance() {
        return balance;
    }

    public void deposit(int amount) {
        balance += amount;
    }
}