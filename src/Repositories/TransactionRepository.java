package Repositories;

import Modal.Transaction;

import java.util.List;

public interface TransactionRepository {
    public void save(Transaction transaction);
    List<Transaction> getAllTransactions();
}
