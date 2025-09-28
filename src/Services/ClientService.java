package Services;

import Modal.Account;
import Modal.Client;
import Modal.Enums.TransactionType;
import Modal.Transaction;
import Repositories.AccountRepository;
import Repositories.ClientRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

public class ClientService {
    private final ClientRepository clientRepository;
    private final AccountRepository accountRepository;
    private final TransactionService transactionService;

    public ClientService(ClientRepository clientRepository,
            AccountRepository accountRepository,
            TransactionService transactionService) {
        this.clientRepository = clientRepository;
        this.accountRepository = accountRepository;
        this.transactionService = transactionService;
    }

    public Client getClientById(String clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new NoSuchElementException("Client introuvable"));
    }

    public Optional<Client> getClientByEmail(String email) {
        return clientRepository.getAllClients().stream()
                .filter(client -> client.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    public List<Account> getAccountsForClient(String clientId) {
        return accountRepository.getAccountsByClientId(clientId);
    }

    public List<Transaction> getTransactionsForClient(String clientId) {
        return transactionService.getTransactionsForClient(clientId);
    }

    public List<Transaction> filterTransactions(String clientId,
            TransactionType type,
            Double minAmount,
            Double maxAmount,
            LocalDate date) {
        return transactionService.filterTransactions(clientId, type, minAmount, maxAmount, date);
    }

    public double getTotalBalanceForClient(String clientId) {
        return transactionService.getTotalBalanceForClient(clientId);
    }

    public Map<TransactionType, Double> getTotalsByTypeForClient(String clientId) {
        return transactionService.getTotalsByTypeForClient(clientId);
    }

    public List<Transaction> detectSuspiciousTransactions(String clientId) {
        return transactionService.detectSuspiciousTransactions(clientId);
    }

    public List<Transaction> sortTransactionsByAmount(String clientId, boolean ascending) {
        return transactionService.sortTransactionsByAmount(clientId, ascending);
    }

    public List<Transaction> sortTransactionsByDate(String clientId, boolean ascending) {
        return transactionService.sortTransactionsByDate(clientId, ascending);
    }
}
