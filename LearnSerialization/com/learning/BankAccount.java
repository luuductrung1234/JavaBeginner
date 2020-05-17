package com.learning;

import java.io.Serializable;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

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

    private String id;
    private int balance = 0;
    private char lastTxType;
    private int lastTxAmount;

    public static final char TX_DEPOSIT = 'd';
    public static final char TX_WITHDRAWAL = 'w';
    public static final char TX_UNKNOWN = 'u';

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
        lastTxType = TX_DEPOSIT;
        lastTxAmount = amount;
    }

    /**
     * synchronized method effected in instance level
     */
    public synchronized void withdrawal(int amount) {
        balance -= amount;
        lastTxType = TX_WITHDRAWAL;
        lastTxAmount = amount;
    }

    public void writeObject(ObjectOutputStream outputStream) throws IOException {
        outputStream.defaultWriteObject();
    }

    public void readObject(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
        ObjectInputStream.GetField fields = inputStream.readFields();
        id = (String) fields.get("id", null);
        balance = fields.get("balance", 0);
        lastTxType = fields.get("lastTxType", TX_UNKNOWN);
        lastTxAmount = fields.get("lastTxAmount", -1);
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