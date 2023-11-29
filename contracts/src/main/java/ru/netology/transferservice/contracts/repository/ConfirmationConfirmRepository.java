package ru.netology.transferservice.contracts.repository;

import ru.netology.transferservice.contracts.entity.Confirmation;

import java.util.UUID;

public interface ConfirmationConfirmRepository {
    Confirmation findLastByTransactionId(UUID transactionId);

    boolean delete(Confirmation confirmation);
}
