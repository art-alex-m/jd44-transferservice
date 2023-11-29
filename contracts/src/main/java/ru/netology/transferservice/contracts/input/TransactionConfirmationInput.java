package ru.netology.transferservice.contracts.input;

/**
 * Интерфейс сценария подтверждения транзакции
 */
public interface TransactionConfirmationInput {
    TransactionConfirmationResponse confirm(TransactionConfirmationRequest request);
}
