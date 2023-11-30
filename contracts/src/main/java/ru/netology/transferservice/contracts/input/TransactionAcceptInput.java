package ru.netology.transferservice.contracts.input;

import ru.netology.transferservice.contracts.exception.TransactionException;

/**
 * Интерфейс сценария проведения транзакции
 */
public interface TransactionAcceptInput {
    TransactionAcceptResponse accept(TransactionAcceptRequest request) throws TransactionException;
}
