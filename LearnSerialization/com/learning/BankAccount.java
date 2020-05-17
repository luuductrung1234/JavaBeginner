package com.learning;

import java.io.Serializable;
import java.io.InvalidClassException;

public class BankAccount implements Serializable {
    /**
     * In serialization, Java calculate version unique identifier. it's a secure
     * hash value that identifies the structure of the class.
     * 
     * This version will be attached when serializing an object to output stream. If
     * there is an update to BankAccount type, the new version with be generated.
     * 
     * When deserializing, Java will compare current version with the version from
     * input stream. If they are not matched, throw InvalidClassException
     * 
     * @see BankAccount
     * @see InvalidClassException
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