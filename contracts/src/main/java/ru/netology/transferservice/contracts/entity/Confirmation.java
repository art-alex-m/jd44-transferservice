package ru.netology.transferservice.contracts.entity;

import java.util.UUID;

/**
 * Код для подтверждения транзакции
 */
public interface Confirmation {
    /**
     * Уникальный код для подтверждения транзакции
     */
    String getCode();

    UUID getTransactionId();
}
