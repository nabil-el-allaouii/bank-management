package Services;

import Modal.Account;
import Modal.Enums.TransactionType;
import Modal.Transaction;
import Repositories.AccountRepository;
import Repositories.TransactionRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

public class TransactionService {
    private static final double SUSPICIOUS_THRESHOLD = 10000.0;

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransactionService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public Transaction addDeposit(String accountId, double amount, String motif) {
        validateAmount(amount);
        Account account = getAccountOrThrow(accountId);
        double newBalance = account.getBalance() + amount;
        accountRepository.updateAccountBalanceById(accountId, newBalance);
        Transaction transaction = new Transaction(accountId, motif, amount, TransactionType.DEPOT, null);
        transactionRepository.save(transaction);
        return transaction;
    }

    public Transaction addWithdrawal(String accountId, double amount, String motif) {
        validateAmount(amount);
        Account account = getAccountOrThrow(accountId);
        if (account.getBalance() < amount) {
            throw new ArithmeticException("Solde insuffisant pour effectuer le retrait");
        }
        double newBalance = account.getBalance() - amount;
        accountRepository.updateAccountBalanceById(accountId, newBalance);
        Transaction transaction = new Transaction(accountId, motif, amount, TransactionType.RETRAIT, null);
        transactionRepository.save(transaction);
        return transaction;
    }

    public Transaction addTransfer(String sourceAccountId, String destinationAccountId, double amount, String motif) {
        validateAmount(amount);
        if (sourceAccountId.equals(destinationAccountId)) {
            throw new IllegalArgumentException("Le compte source et le compte destination doivent être différents");
        }
        Account source = getAccountOrThrow(sourceAccountId);
        Account destination = getAccountOrThrow(destinationAccountId);
        if (source.getBalance() < amount) {
            throw new ArithmeticException("Solde insuffisant pour effectuer le virement");
        }
        accountRepository.updateAccountBalanceById(sourceAccountId, source.getBalance() - amount);
        accountRepository.updateAccountBalanceById(destinationAccountId, destination.getBalance() + amount);
        Transaction transaction = new Transaction(sourceAccountId, motif, amount, TransactionType.VIREMENT,
                destinationAccountId);
        transactionRepository.save(transaction);
        return transaction;
    }

    public Transaction updateTransactionAmount(String transactionId, double newAmount) {
        validateAmount(newAmount);
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new NoSuchElementException("Transaction introuvable"));
        adjustBalancesForUpdatedAmount(transaction, newAmount);
        transaction.setAmount(newAmount);
        transactionRepository.update(transaction);
        return transaction;
    }

    public Transaction updateTransactionMotif(String transactionId, String newMotif) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new NoSuchElementException("Transaction introuvable"));
        transaction.setMotif(newMotif);
        transactionRepository.update(transaction);
        return transaction;
    }

    public void deleteTransaction(String transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new NoSuchElementException("Transaction introuvable"));
        revertBalances(transaction);
        transactionRepository.delete(transactionId);
    }

    public List<Transaction> getTransactionsForAccount(String accountId) {
        return transactionRepository.getTransactionsByAccountId(accountId);
    }

    public List<Transaction> getTransactionsForClient(String clientId) {
        List<Transaction> transactions = new ArrayList<>();
        for (Account account : accountRepository.getAccountsByClientId(clientId)) {
            transactions.addAll(transactionRepository.getTransactionsByAccountId(account.getAccountId()));
        }
        return transactions;
    }

    public List<Transaction> filterTransactions(String clientId,
            TransactionType type,
            Double minAmount,
            Double maxAmount,
            LocalDate date) {
        List<Transaction> filtered = new ArrayList<>();
        for (Transaction transaction : getTransactionsForClient(clientId)) {
            if (type != null && transaction.getTransactionType() != type) {
                continue;
            }
            if (minAmount != null && transaction.getAmount() < minAmount) {
                continue;
            }
            if (maxAmount != null && transaction.getAmount() > maxAmount) {
                continue;
            }
            if (date != null && !transaction.getTransactionDate().toLocalDate().equals(date)) {
                continue;
            }
            filtered.add(transaction);
        }
        filtered.sort((t1, t2) -> t2.getTransactionDate().compareTo(t1.getTransactionDate()));
        return filtered;
    }

    public List<Transaction> sortTransactionsByAmount(String clientId, boolean ascending) {
        List<Transaction> transactions = new ArrayList<>(getTransactionsForClient(clientId));
        transactions.sort((t1, t2) -> ascending
                ? Double.compare(t1.getAmount(), t2.getAmount())
                : Double.compare(t2.getAmount(), t1.getAmount()));
        return transactions;
    }

    public List<Transaction> sortTransactionsByDate(String clientId, boolean ascending) {
        List<Transaction> transactions = new ArrayList<>(getTransactionsForClient(clientId));
        transactions.sort((t1, t2) -> ascending
                ? t1.getTransactionDate().compareTo(t2.getTransactionDate())
                : t2.getTransactionDate().compareTo(t1.getTransactionDate()));
        return transactions;
    }

    public double getTotalBalanceForClient(String clientId) {
        double total = 0.0;
        for (Account account : accountRepository.getAccountsByClientId(clientId)) {
            total += account.getBalance();
        }
        return total;
    }

    public Map<TransactionType, Double> getTotalsByTypeForClient(String clientId) {
        EnumMap<TransactionType, Double> totals = new EnumMap<>(TransactionType.class);
        for (TransactionType type : TransactionType.values()) {
            totals.put(type, 0.0);
        }
        for (Transaction transaction : getTransactionsForClient(clientId)) {
            totals.put(transaction.getTransactionType(),
                    totals.get(transaction.getTransactionType()) + transaction.getAmount());
        }
        return totals;
    }

    public void removeAllTransactionsForAccount(String accountId) {
        List<Transaction> transactions = new ArrayList<>(transactionRepository.getTransactionsByAccountId(accountId));
        for (Transaction transaction : transactions) {
            transactionRepository.delete(transaction.getTransactionId());
        }
    }

    public List<Transaction> detectSuspiciousTransactions(String clientId) {
        List<Transaction> clientTransactions = getTransactionsForClient(clientId);
        Map<String, Integer> dailyCounts = new HashMap<>();
        for (Transaction transaction : clientTransactions) {
            String key = transaction.getTransactionType() + "-" + transaction.getTransactionDate().toLocalDate();
            dailyCounts.put(key, dailyCounts.getOrDefault(key, 0) + 1);
        }

        Set<String> repeatedKeys = new HashSet<>();
        for (Map.Entry<String, Integer> entry : dailyCounts.entrySet()) {
            if (entry.getValue() >= 3) {
                repeatedKeys.add(entry.getKey());
            }
        }

        List<Transaction> suspicious = new ArrayList<>();
        for (Transaction transaction : clientTransactions) {
            boolean largeAmount = transaction.getAmount() >= SUSPICIOUS_THRESHOLD;
            String key = transaction.getTransactionType() + "-" + transaction.getTransactionDate().toLocalDate();
            boolean repeated = repeatedKeys.contains(key);
            if (largeAmount || repeated) {
                suspicious.add(transaction);
            }
        }

        suspicious.sort((t1, t2) -> t2.getTransactionDate().compareTo(t1.getTransactionDate()));
        return suspicious;
    }

    private void adjustBalancesForUpdatedAmount(Transaction transaction, double newAmount) {
        double currentAmount = transaction.getAmount();
        if (currentAmount == newAmount) {
            return;
        }
        Account source = getAccountOrThrow(transaction.getAccountId());
        switch (transaction.getTransactionType()) {
            case DEPOT -> {
                double newBalance = source.getBalance() - currentAmount + newAmount;
                accountRepository.updateAccountBalanceById(source.getAccountId(), newBalance);
            }
            case RETRAIT -> {
                double revertedBalance = source.getBalance() + currentAmount;
                if (revertedBalance < newAmount) {
                    throw new ArithmeticException("Solde insuffisant pour mettre à jour le retrait");
                }
                double newBalance = revertedBalance - newAmount;
                accountRepository.updateAccountBalanceById(source.getAccountId(), newBalance);
            }
            case VIREMENT -> {
                String destinationId = transaction.getAccountDestinationId();
                if (destinationId == null) {
                    throw new IllegalStateException("Transaction de virement sans compte destination");
                }
                Account destination = getAccountOrThrow(destinationId);
                double revertedSource = source.getBalance() + currentAmount;
                double revertedDestination = destination.getBalance() - currentAmount;
                if (revertedSource < newAmount) {
                    throw new ArithmeticException("Solde insuffisant pour mettre à jour le virement");
                }
                accountRepository.updateAccountBalanceById(source.getAccountId(), revertedSource - newAmount);
                accountRepository.updateAccountBalanceById(destination.getAccountId(), revertedDestination + newAmount);
            }
        }
    }

    private void revertBalances(Transaction transaction) {
        Account source = getAccountOrThrow(transaction.getAccountId());
        switch (transaction.getTransactionType()) {
            case DEPOT -> {
                double newBalance = source.getBalance() - transaction.getAmount();
                if (newBalance < 0) {
                    throw new IllegalStateException(
                            "Impossible de supprimer le dépôt car le solde deviendrait négatif");
                }
                accountRepository.updateAccountBalanceById(source.getAccountId(), newBalance);
            }
            case RETRAIT -> accountRepository.updateAccountBalanceById(source.getAccountId(),
                    source.getBalance() + transaction.getAmount());
            case VIREMENT -> {
                String destinationId = transaction.getAccountDestinationId();
                if (destinationId == null) {
                    throw new IllegalStateException("Transaction de virement sans compte destination");
                }
                Account destination = getAccountOrThrow(destinationId);
                accountRepository.updateAccountBalanceById(source.getAccountId(),
                        source.getBalance() + transaction.getAmount());
                double newDestinationBalance = destination.getBalance() - transaction.getAmount();
                if (newDestinationBalance < 0) {
                    throw new IllegalStateException(
                            "Impossible de supprimer le virement car le solde destination deviendrait négatif");
                }
                accountRepository.updateAccountBalanceById(destination.getAccountId(), newDestinationBalance);
            }
        }
    }

    private Account getAccountOrThrow(String accountId) {
        return accountRepository.findByAccountId(accountId)
                .orElseThrow(() -> new NoSuchElementException("Compte introuvable"));
    }

    private void validateAmount(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Le montant doit être positif");
        }
    }
}
