package com.learning;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static Logger logger = Logger.getLogger(Main.class.getSimpleName());

    public static void main(String[] args) {
        demoPersistingObject(new BankAccount("1234", 500), "assets/account.dat");

        List<BankAccount> accounts = new ArrayList<>();
        accounts.add(new BankAccount("1234", 400));
        accounts.add(new BankAccount("3409", 280));
        demoTransientFields(accounts, "assets/group.dat");
    }

    private static void demoPersistingObject(BankAccount bankAccount, String fileName) {
        bankAccount.deposit(100);
        saveObject(bankAccount, fileName);

        BankAccount persistedAccount = loadObject(fileName, BankAccount.class);
        if (persistedAccount != null)
            logger.log(Level.INFO, "Loaded account id:{0}, balance:{1}, lastTxType:{2}, lastTxAmount:{3}",
                    new Object[] { persistedAccount.getId(), persistedAccount.getBalance(),
                            persistedAccount.getLastTxType(), persistedAccount.getLastTxAmount() });
    }

    private static void demoTransientFields(List<BankAccount> accounts, String fileName) {
        AccountGroup accountGroup = new AccountGroup();
        for (BankAccount account : accounts) {
            accountGroup.addAccount(account);
        }
        saveObject(accountGroup, fileName);

        AccountGroup persistedGroup = loadObject(fileName, AccountGroup.class);
        if (persistedGroup != null) {
            var persistedAccounts = persistedGroup.getAccountMap().values();
            logger.log(Level.INFO, "This group contains {0} accounts with total balance is {1}",
                    new Object[] { persistedAccounts.size(), persistedGroup.getTotalBalance() });

            for (BankAccount persistedAccount : persistedAccounts) {
                logger.log(Level.INFO, "Loaded account id:{0}, balance:{1}, lastTxType:{2}, lastTxAmount:{3}",
                        new Object[] { persistedAccount.getId(), persistedAccount.getBalance(),
                                persistedAccount.getLastTxType(), persistedAccount.getLastTxAmount() });
            }
        }
    }

    private static <T> void saveObject(T bankAccount, String fileName) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                Files.newOutputStream(Paths.get(fileName), StandardOpenOption.CREATE))) {
            objectOutputStream.writeObject(bankAccount);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "{0} - {1}", new Object[] { e.getClass().getSimpleName(), e.getMessage() });
        }
    }

    private static <T> T loadObject(String fileName, Class<T> resultType) {
        T account = null;
        try (ObjectInputStream objectInputStream = new ObjectInputStream(
                Files.newInputStream(Paths.get(fileName), StandardOpenOption.CREATE))) {
            account = resultType.cast(objectInputStream.readObject());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "{0} - {1}", new Object[] { e.getClass().getSimpleName(), e.getMessage() });
        }
        return account;
    }
}
