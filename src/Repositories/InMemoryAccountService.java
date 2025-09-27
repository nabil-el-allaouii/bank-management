package Repositories;

import Modal.Account;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class InMemoryAccountService implements AccountRepository{
    private final ArrayList<Account> accounts = new ArrayList<>();

    @Override
    public void save(Account account) {
        accounts.add(account);
    }
    @Override
    public Optional<Account> getAccountById(String id) {
        return accounts.stream().filter(account->account.getAccountId().equals(id)).findFirst();
    }
    @Override
    public Collection<Account> getAllAccounts(){
        return accounts;
    }
}
