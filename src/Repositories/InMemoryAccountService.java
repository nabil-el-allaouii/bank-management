package Repositories;

import Modal.Account;
import Modal.Enums.AccountType;

import java.util.*;

public class InMemoryAccountService implements AccountRepository {
    private final ArrayList<Account> accounts = new ArrayList<>();

    @Override
    public void save(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Account is null");
        }
        accounts.add(account);
    }

    @Override
    public List<Account> getAccountsById(String id) {
        return accounts.stream().filter(account -> account.getClientId().equals(id)).toList();
    }

    @Override
    public Collection<Account> getAllAccounts() {
        return accounts;
    }

    @Override
    public void updateAccountBalanceById(String id, double amount) {
        accounts.stream().filter(account -> account.getAccountId().equals(id))
                .findFirst().orElseThrow(() -> new NoSuchElementException("Account not found"))
                .setBalance(amount);
    }

    @Override
    public void updateAccountTypeById(String id, AccountType type) {
        accounts.stream().filter(account -> account.getAccountId().equals(id)).findFirst()
                .orElseThrow(() -> new NoSuchElementException("Account not found")).setType(type);
    }

    @Override
    public void deleteAccountById(String id) {
        Account accountToRemove = accounts.stream().filter(account->account.getAccountId().equals(id)).findFirst().orElseThrow(()-> new NoSuchElementException("Account not found"));
        accounts.remove(accountToRemove);
    }
}
