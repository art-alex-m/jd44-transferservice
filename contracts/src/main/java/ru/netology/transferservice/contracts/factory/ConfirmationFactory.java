package ru.netology.transferservice.contracts.factory;

import ru.netology.transferservice.contracts.entity.Confirmation;

import java.util.UUID;

public interface ConfirmationFactory {
    Confirmation create(UUID transactionId);
}
