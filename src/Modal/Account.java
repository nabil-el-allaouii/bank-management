package Modal;

import Modal.Enums.AccountType;

import java.util.UUID;

public class Account {
    private String accountId;
    private AccountType accountType;
    private double balance;
    private String clientId;

    public Account(AccountType accountType , double balance, String clientId){
        this.accountType = accountType;
        this.balance = balance;
        this.clientId = clientId;
        this.accountId = UUID.randomUUID().toString();
    }
    public String getAccountId() {
        return accountId;
    }
    public AccountType getAccountType() {
        return accountType;
    }
    public double getBalance() {
        return balance;
    }
    public String getClientId() {
        return clientId;
    }
}
