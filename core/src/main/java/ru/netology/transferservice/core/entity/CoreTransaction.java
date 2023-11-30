package ru.netology.transferservice.core.entity;

import ru.netology.transferservice.contracts.entity.Card;
import ru.netology.transferservice.contracts.entity.Commission;
import ru.netology.transferservice.contracts.entity.Transaction;
import ru.netology.transferservice.contracts.entity.TransactionStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public class CoreTransaction implements Transaction {
    private final UUID id;
    private final Card cardFrom;
    private final Card cardTo;
    private final long amount;
    private final String currency;
    private final TransactionStatus status;
    private final Commission commission;
    private final LocalDateTime createdAt;

    public CoreTransaction(UUID id, Card cardFrom, Card cardTo, long amount, String currency,
            TransactionStatus status, Commission commission, LocalDateTime createdAt) {
        this.id = id;
        this.cardFrom = cardFrom;
        this.cardTo = cardTo;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
        this.commission = commission;
        this.createdAt = createdAt;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public Card getCardFrom() {
        return cardFrom;
    }

    @Override
    public Card getCardTo() {
        return cardTo;
    }

    @Override
    public long getAmount() {
        return amount;
    }

    @Override
    public long getAmountWithCommission() {
        return amount + commission.getAmount();
    }

    @Override
    public String getCurrency() {
        return currency;
    }

    @Override
    public TransactionStatus getStatus() {
        return status;
    }

    @Override
    public Commission getCommission() {
        return commission;
    }

    @Override
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
