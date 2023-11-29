package ru.netology.transferservice.contracts.entity;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Транзакция по переводу денег
 */
public interface Transaction {
    UUID getId();

    Card getCardFrom();

    Card getCardTo();

    long getAmount();

    long getAmountWithCommission();

    String getCurrency();

    TransactionStatus getStatus();

    Commission getCommission();

    LocalDateTime getCreatedAt();
}
