package ru.netology.transferservice.contracts.service;

import ru.netology.transferservice.contracts.entity.Transaction;

public interface TransactionLogger {
    void log(Transaction transaction);
}
