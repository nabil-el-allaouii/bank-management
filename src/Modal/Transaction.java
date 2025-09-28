package Modal;

import Modal.Enums.TransactionType;

import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {
    private final String transactionId;
    private final String accountId;
    private String motif;
    private String accountDestinationId;
    private double amount;
    private final TransactionType transactionType;
    private final LocalDateTime transactionDate;

    public Transaction(String accountId, String motif, double amount, TransactionType transactionType,
            String accountDestinationId) {
        this(UUID.randomUUID().toString(), accountId, motif, amount, transactionType, accountDestinationId,
                LocalDateTime.now());
    }

    public Transaction(String transactionId, String accountId, String motif, double amount,
            TransactionType transactionType, String accountDestinationId, LocalDateTime transactionDate) {
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.motif = motif;
        this.accountDestinationId = accountDestinationId;
        this.amount = amount;
        this.transactionType = transactionType;
        this.transactionDate = transactionDate == null ? LocalDateTime.now() : transactionDate;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public String getAccountDestinationId() {
        return accountDestinationId;
    }

    public void setAccountDestinationId(String accountDestinationId) {
        this.accountDestinationId = accountDestinationId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }
}
