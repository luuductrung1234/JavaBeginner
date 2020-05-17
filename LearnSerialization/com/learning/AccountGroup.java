package com.learning;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class AccountGroup implements Serializable {
    private static final long serialVersionUID = 1L;
    private Map<String, BankAccount> accountMap = new HashMap<>();
    /**
     * make this field transient from serialization
     */
    private transient int totalBalance;

    public void addAccount(BankAccount account) {
        totalBalance += account.getBalance();
        accountMap.put(account.getId(), account);
    }

    /**
     * @return the accountMap
     */
    public Map<String, BankAccount> getAccountMap() {
        return accountMap;
    }

    /**
     * @return the totalBalance
     */
    public int getTotalBalance() {
        return totalBalance;
    }
}