package ru.netology.transferservice.contracts.input;

import ru.netology.transferservice.contracts.exception.TransactionException;

/**
 * Интерфейс сценария создания транзакции
 */
public interface TransactionCreateInput {
    TransactionCreateResponse create(TransactionCreateRequest request) throws TransactionException;
}
