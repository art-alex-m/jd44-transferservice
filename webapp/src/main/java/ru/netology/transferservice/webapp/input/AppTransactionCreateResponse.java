package ru.netology.transferservice.webapp.input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.netology.transferservice.contracts.input.TransactionCreateResponse;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class AppTransactionCreateResponse {
    private final UUID operationId;

    public AppTransactionCreateResponse(TransactionCreateResponse response) {
        this(response.getTransactionId());
    }
}
