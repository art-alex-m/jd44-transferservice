package ru.netology.transferservice.core.input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.netology.transferservice.contracts.entity.Confirmation;
import ru.netology.transferservice.contracts.input.TransactionConfirmationCreateResponse;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class CoreTransactionConfirmationCreateResponse implements TransactionConfirmationCreateResponse {
    private final UUID transactionId;
    private final String code;

    public CoreTransactionConfirmationCreateResponse(Confirmation confirmation) {
        this(confirmation.getTransactionId(), confirmation.getCode());
    }
}
