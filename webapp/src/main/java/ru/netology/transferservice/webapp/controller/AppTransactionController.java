package ru.netology.transferservice.webapp.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.netology.transferservice.contracts.input.TransactionConfirmationInput;
import ru.netology.transferservice.contracts.input.TransactionCreateInput;
import ru.netology.transferservice.webapp.input.AppTransactionConfirmationRequest;
import ru.netology.transferservice.webapp.input.AppTransactionConfirmationResponse;
import ru.netology.transferservice.webapp.input.AppTransactionCreateRequest;
import ru.netology.transferservice.webapp.input.AppTransactionCreateResponse;

@RestController
public class AppTransactionController {
    private final TransactionCreateInput transactionCreateInput;
    private final TransactionConfirmationInput transactionConfirmationInput;

    public AppTransactionController(TransactionCreateInput transactionCreateInput,
                                 TransactionConfirmationInput transactionConfirmationInput) {
        this.transactionCreateInput = transactionCreateInput;
        this.transactionConfirmationInput = transactionConfirmationInput;
    }

    @PostMapping("/transfer")
    public AppTransactionCreateResponse createTransfer(@Validated @RequestBody AppTransactionCreateRequest request) {
        return new AppTransactionCreateResponse(transactionCreateInput.create(request));
    }

    @PostMapping("/confirmOperation")
    public AppTransactionConfirmationResponse confirmOperation(@Validated @RequestBody AppTransactionConfirmationRequest request) {
        return new AppTransactionConfirmationResponse(transactionConfirmationInput.confirm(request));
    }
}
