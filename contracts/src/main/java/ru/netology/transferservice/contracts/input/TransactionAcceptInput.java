package ru.netology.transferservice.contracts.input;

/**
 * Интерфейс сценария проведения транзакции
 */
public interface TransactionAcceptInput {
    TransactionAcceptResponse accept(TransactionAcceptRequest request);
}
