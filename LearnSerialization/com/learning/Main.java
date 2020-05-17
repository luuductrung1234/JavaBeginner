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
        saveAccount(bankAccount, "account.dat");

        BankAccount persistedAccount = loadAccount("account.dat");
        logger.log(Level.INFO, "Loaded account id:{0}, balance:{1}",
                new Object[] { persistedAccount.getId(), persistedAccount.getBalance() });
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
        try (ObjectInputStream objectInputStream = new ObjectInputStream(
                Files.newInputStream(Paths.get(fileName), StandardOpenOption.CREATE))) {
            return (BankAccount) objectInputStream.readObject();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "{0} - {1}", new Object[] { e.getClass().getSimpleName(), e.getMessage() });
        }
    }
}
