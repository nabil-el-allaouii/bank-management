package Repositories;

import Modal.AccountManager;

import java.util.List;
import java.util.Optional;

public interface ManagerRepository {
    public void Save(AccountManager accountManager);
    Optional<AccountManager> findByEmail(String email);
    List<AccountManager> getAllManagers();
}
