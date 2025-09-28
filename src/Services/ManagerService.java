package Services;

import Modal.Account;
import Modal.Client;
import Modal.Enums.AccountType;
import Repositories.AccountRepository;
import Repositories.ClientRepository;

import java.util.List;
import java.util.NoSuchElementException;

public class ManagerService {
    private final ClientRepository clientRepo;
    private final AccountRepository accountRepo;

    public ManagerService(ClientRepository clientRepo, AccountRepository accountRepo) {
        this.clientRepo = clientRepo;
        this.accountRepo = accountRepo;
    }

    public void updateClientNameById(String clientId, String name) {
        clientRepo.updateClientNameById(clientId, name);
    }

    public void updateClientEmailById(String clientId, String email) {
        clientRepo.updateClientEmailById(clientId, email);
    }

    public void updateClientPasswordById(String clientId, String password) {
        clientRepo.updateClientPasswordById(clientId, password);
    }

    public List<Client> getAllClients() {
        return clientRepo.getAllClients();
    }

    public void removeClientById(String clientId) {
        clientRepo.deleteClientById(clientId);
    }

    public void createAccountForClient(AccountType accountType, double balance, String clientId) {
        clientRepo.findById(clientId).orElseThrow(() -> new NoSuchElementException("Client not found"));
        if (balance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }
        Account account = new Account(accountType, balance, clientId);
        accountRepo.save(account);
    }

    public void updateAccountBalanceById(String accountId, double amount) {
        accountRepo.updateAccountBalanceById(accountId, amount);
    }

    public void updateAccountTypeById(String accountId, AccountType type) {
        accountRepo.updateAccountTypeById(accountId, type);
    }

    public void deleteAccountById(String accountId) {
        accountRepo.deleteAccountById(accountId);
    }

    public List<Account> getAccountsByClientId(String clientId) {
        return accountRepo.getAccountsByClientId(clientId);
    }
}
