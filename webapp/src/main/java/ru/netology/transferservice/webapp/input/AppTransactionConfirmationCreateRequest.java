package ru.netology.transferservice.webapp.input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.netology.transferservice.contracts.input.TransactionConfirmationCreateRequest;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class AppTransactionConfirmationCreateRequest implements TransactionConfirmationCreateRequest {
    private final UUID transactionId;
}
