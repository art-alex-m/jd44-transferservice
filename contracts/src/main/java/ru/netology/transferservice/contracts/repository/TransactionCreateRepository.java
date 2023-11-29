package ru.netology.transferservice.contracts.repository;

import ru.netology.transferservice.contracts.entity.Transaction;

public interface TransactionCreateRepository {
    boolean store(Transaction transaction);
}
