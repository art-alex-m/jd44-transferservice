package ru.netology.transferservice.contracts.input;

import ru.netology.transferservice.contracts.exception.TransactionException;

public interface TransactionConfirmationCreateInput {
    TransactionConfirmationCreateResponse create(TransactionConfirmationCreateRequest request) throws TransactionException;
}
