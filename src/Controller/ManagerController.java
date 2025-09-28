package Controller;

import Modal.Account;
import Modal.Client;
import Modal.Enums.AccountType;
import Modal.Enums.TransactionType;
import Modal.Transaction;
import Services.ManagerService;
import Services.TransactionService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class ManagerController {
    private final ManagerService managerService;
    private final TransactionService transactionService;

    public ManagerController(ManagerService managerService, TransactionService transactionService) {
        this.managerService = managerService;
        this.transactionService = transactionService;
    }

    public void updateClientNameById(String clientId, String name) {
        try {
            managerService.updateClientNameById(clientId, name);
            System.out.println("Client Name Updated Successfully To " + name);
        } catch (NoSuchElementException e) {
            System.out.println("No Such Client" + e.getMessage());
        }
    }

    public void updateClientEmailById(String clientId, String email) {
        try {
            managerService.updateClientEmailById(clientId, email);
            System.out.println("Client Email Updated Successfully To " + email);
        } catch (NoSuchElementException e) {
            System.out.println("No Such Client" + e.getMessage());
        }
    }

    public void updateClientPasswordById(String clientId, String password) {
        try {
            managerService.updateClientPasswordById(clientId, password);
            System.out.println("Client Password Updated Successfully To " + password);
        } catch (NoSuchElementException e) {
            System.out.println("No Such Client" + e.getMessage());
        }
    }

    public List<Client> getAllClients() {
        return managerService.getAllClients();
    }

    public void deleteClientById(String clientId) {
        try {
            List<Account> accounts = managerService.getAccountsByClientId(clientId);
            for (Account account : accounts) {
                transactionService.removeAllTransactionsForAccount(account.getAccountId());
                managerService.deleteAccountById(account.getAccountId());
            }
            managerService.removeClientById(clientId);
            System.out.println("Client Deleted Successfully");
        } catch (NoSuchElementException e) {
            System.out.println("No Such Client" + e.getMessage());
        }
    }

    public void createAccountForClient(AccountType accountType, double balance, String clientId) {
        try {
            managerService.createAccountForClient(accountType, balance, clientId);
            System.out.println("Account Created Successfully To " + clientId);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void updateAccountBalanceById(String accountId, double amount) {
        try {
            managerService.updateAccountBalanceById(accountId, amount);
            System.out.println("Account Balance Updated Successfully To " + amount);
        } catch (NoSuchElementException e) {
            System.out.println("No Such Account" + e.getMessage());
        }
    }

    public void updateAccountTypeById(String accountId, AccountType type) {
        try {
            managerService.updateAccountTypeById(accountId, type);
            System.out.println("Account Type Updated Successfully To " + type);
        } catch (NoSuchElementException e) {
            System.out.println("No Such Account" + e.getMessage());
        }
    }

    public void deleteAccountById(String accountId) {
        try {
            transactionService.removeAllTransactionsForAccount(accountId);
            managerService.deleteAccountById(accountId);
            System.out.println("Account Deleted Successfully ");
        } catch (NoSuchElementException e) {
            System.out.println("No Such Account" + e.getMessage());
        }
    }

    public List<Account> getAccountByClientId(String clientId) {
        try {
            return managerService.getAccountsByClientId(clientId);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("No Such Client" + e.getMessage());
        }
    }

    public void addDeposit(String accountId, double amount, String motif) {
        try {
            transactionService.addDeposit(accountId, amount, motif);
            System.out.println("Deposit added successfully");
        } catch (RuntimeException e) {
            System.out.println("Failed to add deposit: " + e.getMessage());
        }
    }

    public void addWithdrawal(String accountId, double amount, String motif) {
        try {
            transactionService.addWithdrawal(accountId, amount, motif);
            System.out.println("Withdrawal added successfully");
        } catch (RuntimeException e) {
            System.out.println("Failed to add withdrawal: " + e.getMessage());
        }
    }

    public void addTransfer(String sourceAccountId, String destinationAccountId, double amount, String motif) {
        try {
            transactionService.addTransfer(sourceAccountId, destinationAccountId, amount, motif);
            System.out.println("Transfer completed successfully");
        } catch (RuntimeException e) {
            System.out.println("Failed to complete transfer: " + e.getMessage());
        }
    }

    public void updateTransactionAmount(String transactionId, double newAmount) {
        try {
            transactionService.updateTransactionAmount(transactionId, newAmount);
            System.out.println("Transaction amount updated successfully");
        } catch (RuntimeException e) {
            System.out.println("Failed to update transaction amount: " + e.getMessage());
        }
    }

    public void updateTransactionMotif(String transactionId, String newMotif) {
        try {
            transactionService.updateTransactionMotif(transactionId, newMotif);
            System.out.println("Transaction motif updated successfully");
        } catch (RuntimeException e) {
            System.out.println("Failed to update transaction motif: " + e.getMessage());
        }
    }

    public void deleteTransaction(String transactionId) {
        try {
            transactionService.deleteTransaction(transactionId);
            System.out.println("Transaction deleted successfully");
        } catch (RuntimeException e) {
            System.out.println("Failed to delete transaction: " + e.getMessage());
        }
    }

    public List<Transaction> getTransactionsForAccount(String accountId) {
        return transactionService.getTransactionsForAccount(accountId);
    }

    public List<Transaction> getTransactionsForClient(String clientId) {
        return transactionService.getTransactionsForClient(clientId);
    }

    public List<Transaction> filterTransactions(String clientId, TransactionType type, Double minAmount,
            Double maxAmount, LocalDate date) {
        return transactionService.filterTransactions(
                clientId,
                type,
                minAmount,
                maxAmount,
                date);
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
