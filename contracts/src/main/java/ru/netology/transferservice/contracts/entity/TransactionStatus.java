package ru.netology.transferservice.contracts.entity;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Статус транзакции
 */
public interface TransactionStatus {
    UUID getId();

    TransactionStatusCode getCode();

    /**
     * Дополнительное сообщение, описывающее статус транзакции
     */
    String getMessage();

    LocalDateTime getCreatedAt();
}
