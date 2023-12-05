package ru.netology.transferservice.webapp.input;

import ru.netology.transferservice.contracts.input.TransactionCreateResponse;
import ru.netology.transferservice.webapp.model.AppResponse;

public class AppTransactionCreateResponse extends AppResponse {
    public AppTransactionCreateResponse(TransactionCreateResponse response) {
        super(response.getTransactionId());
    }
}
