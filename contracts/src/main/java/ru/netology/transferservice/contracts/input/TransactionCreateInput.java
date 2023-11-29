package ru.netology.transferservice.contracts.input;

/**
 * Интерфейс сценария создания транзакции
 */
public interface TransactionCreateInput {
    TransactionCreateResponse create(TransactionCreateRequest request);
}
