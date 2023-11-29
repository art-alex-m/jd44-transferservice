package ru.netology.transferservice.contracts.entity;

import java.util.UUID;

/**
 * Зарезервированная сумма для транзакции
 */
public interface AccountAllocation extends AccountNumbering {
    UUID getId();

    UUID getTransactionId();

    long getAmount();
}
