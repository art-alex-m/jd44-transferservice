package ru.netology.transferservice.core.input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.netology.transferservice.contracts.entity.TransactionStatus;
import ru.netology.transferservice.contracts.input.TransactionConfirmationResponse;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class CoreTransactionConfirmationResponse implements TransactionConfirmationResponse {
    private final UUID transactionId;
    private final TransactionStatus status;
}
