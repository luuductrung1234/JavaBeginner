package com.learning;

import java.io.Serializable;

public class BankAccount implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final String id;
    private int balance = 0;
    private char lastTxType;
    private int lastTxAmount = 0;

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
     * synchronized method effected in instance level
     */
    public synchronized void deposit(int amount) {
        balance += amount;
        lastTxType = 'd';
        lastTxAmount = amount;
    }

    /**
     * synchronized method effected in instance level
     */
    public synchronized void withdrawal(int amount) {
        balance -= amount;
        lastTxType = 'w';
        lastTxAmount = amount;
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
     * @return the lastTxType
     */
    public char getLastTxType() {
        return lastTxType;
    }

    /**
     * @return the lastTxAmount
     */
    public int getLastTxAmount() {
        return lastTxAmount;
    }
}