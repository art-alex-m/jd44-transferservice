package ru.netology.transferservice.webapp.input;

import ru.netology.transferservice.contracts.input.TransactionConfirmationResponse;
import ru.netology.transferservice.webapp.model.AppResponse;

public class AppTransactionConfirmationResponse extends AppResponse {
    public AppTransactionConfirmationResponse(TransactionConfirmationResponse response) {
        super(response.getTransactionId());
    }
}
