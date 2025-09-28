package Controller;

import Modal.Account;
import Modal.Client;
import Modal.Enums.TransactionType;
import Modal.Transaction;
import Services.ClientService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    public Client getClientProfile(String clientId) {
        try {
            return clientService.getClientById(clientId);
        } catch (NoSuchElementException e) {
            System.out.println("Client introuvable : " + e.getMessage());
            throw e;
        }
    }

    public List<Account> getClientAccounts(String clientId) {
        return clientService.getAccountsForClient(clientId);
    }

    public List<Transaction> getClientTransactions(String clientId) {
        return clientService.getTransactionsForClient(clientId);
    }

    public List<Transaction> filterTransactions(String clientId,
            TransactionType type,
            Double minAmount,
            Double maxAmount,
            LocalDate date) {
        return clientService.filterTransactions(clientId, type, minAmount, maxAmount, date);
    }

    public double getTotalBalance(String clientId) {
        return clientService.getTotalBalanceForClient(clientId);
    }

    public Map<TransactionType, Double> getTotalsByType(String clientId) {
        return clientService.getTotalsByTypeForClient(clientId);
    }

    public List<Transaction> detectSuspiciousTransactions(String clientId) {
        return clientService.detectSuspiciousTransactions(clientId);
    }

    public List<Transaction> sortTransactionsByAmount(String clientId, boolean ascending) {
        return clientService.sortTransactionsByAmount(clientId, ascending);
    }

    public List<Transaction> sortTransactionsByDate(String clientId, boolean ascending) {
        return clientService.sortTransactionsByDate(clientId, ascending);
    }
}
