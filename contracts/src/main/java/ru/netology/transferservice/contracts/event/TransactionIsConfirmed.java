package ru.netology.transferservice.contracts.event;

import ru.netology.transferservice.contracts.entity.Transaction;

import java.time.LocalDateTime;

public class TransactionIsConfirmed implements TransactionEvent {
    private final Transaction transaction;
    private final LocalDateTime createdAt;

    public TransactionIsConfirmed(Transaction transaction) {
        this.transaction = transaction;
        this.createdAt = LocalDateTime.now();
    }


    @Override
    public Transaction getTransaction() {
        return transaction;
    }

    @Override
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
