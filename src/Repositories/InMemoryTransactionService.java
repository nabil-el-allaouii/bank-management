package Repositories;

import Modal.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class InMemoryTransactionService implements TransactionRepository {
    private final List<Transaction> transactions = new ArrayList<>();

    @Override
    public void save(Transaction transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction is null");
        }
        transactions.add(transaction);
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return new ArrayList<>(transactions);
    }

    @Override
    public List<Transaction> getTransactionsByAccountId(String accountId) {
        return transactions.stream()
                .filter(transaction -> transaction.getAccountId().equals(accountId))
                .toList();
    }

    @Override
    public Optional<Transaction> findById(String transactionId) {
        return transactions.stream()
                .filter(transaction -> transaction.getTransactionId().equals(transactionId))
                .findFirst();
    }

    @Override
    public void update(Transaction transaction) {
        int index = -1;
        for (int i = 0; i < transactions.size(); i++) {
            if (transactions.get(i).getTransactionId().equals(transaction.getTransactionId())) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            throw new NoSuchElementException("Transaction not found");
        }
        transactions.set(index, transaction);
    }

    @Override
    public void delete(String transactionId) {
        Transaction transactionToRemove = findById(transactionId)
                .orElseThrow(() -> new NoSuchElementException("Transaction not found"));
        transactions.remove(transactionToRemove);
    }
}
