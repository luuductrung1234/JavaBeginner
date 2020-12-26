package com.learning;

public class BankAccount {
    private final String id;
    private int balance;

    public BankAccount(String id) {
        super();
        this.id = id;
    }

    public BankAccount(String id, int startBalance) {
        super();
        this.id = id;
        balance = startBalance;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
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