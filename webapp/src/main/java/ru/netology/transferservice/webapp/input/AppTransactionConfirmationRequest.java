package ru.netology.transferservice.webapp.input;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.netology.transferservice.contracts.input.TransactionConfirmationRequest;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class AppTransactionConfirmationRequest implements TransactionConfirmationRequest {
    @NotNull
    @JsonAlias("operationId")
    private final UUID transactionId;

    @NotEmpty
    private final String code;
}
