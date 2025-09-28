package Repositories;

import Modal.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository {
    public void save(Transaction transaction);

    List<Transaction> getAllTransactions();

    List<Transaction> getTransactionsByAccountId(String accountId);

    Optional<Transaction> findById(String transactionId);

    void update(Transaction transaction);

    void delete(String transactionId);
}
