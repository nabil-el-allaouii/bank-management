package Repositories;

import Modal.AccountManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryManagerService implements ManagerRepository{
    private final ArrayList<AccountManager> accountManagers = new ArrayList<>();

    @Override
    public void Save(AccountManager accountManager) {
        accountManagers.add(accountManager);
    }
    @Override
    public Optional<AccountManager> findByEmail(String email) {
        return accountManagers.stream().filter(account -> account.getEmail().equals(email)).findFirst();
    }
    @Override
    public List<AccountManager> getAllManagers() {
        return accountManagers;
    }
}
