package ru.netology.transferservice.core.factory;

import ru.netology.transferservice.contracts.entity.TransactionStatus;
import ru.netology.transferservice.contracts.entity.TransactionStatusCode;
import ru.netology.transferservice.contracts.factory.TransactionStatusFactory;
import ru.netology.transferservice.core.entity.CoreTransactionStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public class CoreTransactionStatusFactory implements TransactionStatusFactory {
    @Override
    public TransactionStatus create(TransactionStatusCode code, String message) {
        return new CoreTransactionStatus(UUID.randomUUID(), code, message, LocalDateTime.now());
    }
}
