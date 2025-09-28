package Repositories;

import Modal.Account;
import Modal.Enums.AccountType;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface AccountRepository {
    public void save(Account account);

    List<Account> getAccountsByClientId(String clientId);

    Collection<Account> getAllAccounts();

    Optional<Account> findByAccountId(String accountId);

    public void updateAccountBalanceById(String id, double amount);

    public void updateAccountTypeById(String id, AccountType type);

    public void deleteAccountById(String id);
}
