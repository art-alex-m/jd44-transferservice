package ru.netology.transferservice.contracts.input;

import java.util.UUID;

public interface TransactionIdentificator {
    UUID getTransactionId();
}
