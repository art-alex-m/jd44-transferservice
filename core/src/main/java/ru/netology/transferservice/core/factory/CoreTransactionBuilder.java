package ru.netology.transferservice.core.factory;

import ru.netology.transferservice.contracts.entity.Card;
import ru.netology.transferservice.contracts.entity.Commission;
import ru.netology.transferservice.contracts.entity.Transaction;
import ru.netology.transferservice.contracts.entity.TransactionStatus;
import ru.netology.transferservice.contracts.factory.TransactionBuilder;
import ru.netology.transferservice.core.entity.CoreTransaction;

import java.time.LocalDateTime;
import java.util.UUID;

public class CoreTransactionBuilder implements TransactionBuilder {
    private UUID id;
    private Card cardFrom;
    private Card cardTo;
    private long amount;
    private String currency;
    private TransactionStatus status;
    private Commission commission;
    private LocalDateTime createdAt;

    @Override
    public Transaction build() {
        if (id == null) {
            id = UUID.randomUUID();
        }
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }

        return new CoreTransaction(id, cardFrom, cardTo, amount, currency, status, commission, createdAt);
    }

    @Override
    public TransactionBuilder setId(UUID id) {
        this.id = id;
        return this;
    }

    @Override
    public TransactionBuilder setCardFrom(Card card) {
        this.cardFrom = card;
        return this;
    }

    @Override
    public TransactionBuilder setCardTo(Card card) {
        this.cardTo = card;
        return this;
    }

    @Override
    public TransactionBuilder setAmount(long amount) {
        this.amount = amount;
        return this;
    }

    @Override
    public TransactionBuilder setCurrency(String currency) {
        this.currency = currency;
        return this;
    }

    @Override
    public TransactionBuilder setStatus(TransactionStatus status) {
        this.status = status;
        return this;
    }

    @Override
    public TransactionBuilder setCommission(Commission commission) {
        this.commission = commission;
        return this;
    }

    @Override
    public TransactionBuilder setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    @Override
    public TransactionBuilder toBuilder(Transaction base) {
        return this.setId(base.getId())
                .setCardFrom(base.getCardFrom())
                .setCardTo(base.getCardTo())
                .setAmount(base.getAmount())
                .setCurrency(base.getCurrency())
                .setCreatedAt(base.getCreatedAt())
                .setStatus(base.getStatus())
                .setCommission(base.getCommission());
    }
}
