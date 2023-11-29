package ru.netology.transferservice.contracts.repository;

import ru.netology.transferservice.contracts.entity.Transaction;

import java.util.UUID;

public interface TransactionAcceptRepository {
    Transaction getById(UUID transactionId);

    boolean update(Transaction transaction);
}
