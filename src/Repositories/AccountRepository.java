package Repositories;

import Modal.Account;

import java.util.Collection;
import java.util.Optional;

public interface AccountRepository {
    public void save(Account account);
    Optional<Account> getAccountById(String id);
    Collection<Account> getAllAccounts();
}
