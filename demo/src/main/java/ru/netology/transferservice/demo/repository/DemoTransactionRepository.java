package ru.netology.transferservice.demo.repository;

import ru.netology.transferservice.contracts.entity.Transaction;
import ru.netology.transferservice.contracts.repository.TransactionAcceptRepository;
import ru.netology.transferservice.contracts.repository.TransactionConfirmRepository;
import ru.netology.transferservice.contracts.repository.TransactionCreateRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DemoTransactionRepository implements TransactionCreateRepository, TransactionConfirmRepository,
        TransactionAcceptRepository {

    private final Map<UUID, Transaction> storage = new HashMap<>();

    @Override
    public boolean store(Transaction transaction) {
        storage.put(transaction.getId(), transaction);
        return true;
    }

    @Override
    public Transaction getById(UUID transactionId) {
        return storage.get(transactionId);
    }

    @Override
    public boolean update(Transaction transaction) {
        storage.replace(transaction.getId(), transaction);
        return true;
    }
}
