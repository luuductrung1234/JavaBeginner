package com.learning;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static Logger logger = Logger.getLogger(Main.class.getSimpleName());

    public static void main(String[] args) {
        BankAccount bankAccount = new BankAccount("1234", 500);
        bankAccount.deposit(100);
        saveAccount(bankAccount, "assets/account.dat");

        BankAccount persistedAccount = loadAccount("assets/account.dat");
        if (persistedAccount != null)
            logger.log(Level.INFO, "Loaded account id:{0}, balance:{1}, lastTxType:{2}, lastTxAmount:{3}",
                    new Object[] { persistedAccount.getId(), persistedAccount.getBalance(),
                            persistedAccount.getLastTxType(), persistedAccount.getLastTxAmount() });
    }

    public static void saveAccount(BankAccount bankAccount, String fileName) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                Files.newOutputStream(Paths.get(fileName), StandardOpenOption.CREATE))) {
            objectOutputStream.writeObject(bankAccount);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "{0} - {1}", new Object[] { e.getClass().getSimpleName(), e.getMessage() });
        }
    }

    public static BankAccount loadAccount(String fileName) {
        BankAccount account = null;
        try (ObjectInputStream objectInputStream = new ObjectInputStream(
                Files.newInputStream(Paths.get(fileName), StandardOpenOption.CREATE))) {
            account = (BankAccount) objectInputStream.readObject();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "{0} - {1}", new Object[] { e.getClass().getSimpleName(), e.getMessage() });
        }
        return account;
    }
}
