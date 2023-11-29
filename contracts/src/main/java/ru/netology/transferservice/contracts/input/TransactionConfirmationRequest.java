package ru.netology.transferservice.contracts.input;

/**
 * Запрос на подтверждение транзакции
 */
public interface TransactionConfirmationRequest extends TransactionIdentificator {
    /**
     * Код подтверждения транзакции
     */
    String getCode();
}
