package org.researching;

public class BankAccount {
    private int balance;

    public BankAccount(int startBalance) {
        super();
        balance = startBalance;
    }

    /**
     * synchronized method effected in instance level
     * 
     * @return the balance
     */
    public synchronized int getBalance() {
        return balance;
    }

    /**
     * synchronized method effected in instance level
     */
    public synchronized void deposit(int amount) {
        balance += amount;
    }

    /**
     * synchronized method effected in instance level
     */
    public synchronized void withdrawal(int amount) {
        balance -= amount;
    }
}