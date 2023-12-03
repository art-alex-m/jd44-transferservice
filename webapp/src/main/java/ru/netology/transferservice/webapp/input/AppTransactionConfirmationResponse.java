package ru.netology.transferservice.webapp.input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.netology.transferservice.contracts.input.TransactionConfirmationResponse;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class AppTransactionConfirmationResponse {
    private final UUID operationId;

    public AppTransactionConfirmationResponse(TransactionConfirmationResponse response) {
        this(response.getTransactionId());
    }
}
