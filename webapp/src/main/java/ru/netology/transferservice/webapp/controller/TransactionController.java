package ru.netology.transferservice.webapp.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.netology.transferservice.contracts.input.TransactionCreateInput;
import ru.netology.transferservice.webapp.input.AppTransactionCreateRequest;
import ru.netology.transferservice.webapp.input.AppTransactionCreateResponse;

@RestController
public class TransactionController {
    private final TransactionCreateInput transactionCreateInput;

    public TransactionController(TransactionCreateInput transactionCreateInput) {
        this.transactionCreateInput = transactionCreateInput;
    }

    @PostMapping("/transfer")
    public AppTransactionCreateResponse createTransfer(@Validated @RequestBody AppTransactionCreateRequest request) {
        return new AppTransactionCreateResponse(transactionCreateInput.create(request));
    }
}
