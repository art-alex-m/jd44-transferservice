package ru.netology.transferservice.contracts.input;

import ru.netology.transferservice.contracts.exception.TransactionException;

/**
 * Интерфейс сценария подтверждения транзакции
 */
public interface TransactionConfirmationInput {
    TransactionConfirmationResponse confirm(TransactionConfirmationRequest request) throws TransactionException;
}
