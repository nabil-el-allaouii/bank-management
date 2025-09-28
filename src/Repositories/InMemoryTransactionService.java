package Repositories;

import Modal.Transaction;

import java.util.ArrayList;
import java.util.List;

public class InMemoryTransactionService implements TransactionRepository{
    private final TransactionRepository transactionRepository;
    private final ArrayList<Transaction> transactions = new ArrayList<>();
    public InMemoryTransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void save(Transaction transaction) {
        transactionRepository.save(transaction);
    }
    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.getAllTransactions();
    }

}
