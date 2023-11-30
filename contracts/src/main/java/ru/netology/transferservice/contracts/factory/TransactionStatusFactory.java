package ru.netology.transferservice.contracts.factory;

import ru.netology.transferservice.contracts.entity.TransactionStatus;
import ru.netology.transferservice.contracts.entity.TransactionStatusCode;

public interface TransactionStatusFactory {
    TransactionStatus create(TransactionStatusCode code, String message);

    default TransactionStatus create(TransactionStatusCode code) {
        return create(code, null);
    }

    ;
}
