package Modal;

import Modal.Enums.TransactionType;

import java.util.UUID;

public class Transaction {
    private final String transactionId;
    private final String accountId;
    private final String motif;
    private final String accountDestinationId;
    private final double amount;
    private final TransactionType transactionType;

    public Transaction(String accountId , String motif , double amount, TransactionType transactionType , String accountDestinationId){
        this.transactionId = UUID.randomUUID().toString();
        this.accountId = accountId;
        this.motif = motif;
        this.accountDestinationId = accountDestinationId;
        this.amount = amount;
        this.transactionType = transactionType;
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
    public String getAccountDestinationId() {
        return accountDestinationId;
    }
    public double getAmount() {
        return amount;
    }
    public TransactionType getTransactionType() {
        return transactionType;
    }
}
