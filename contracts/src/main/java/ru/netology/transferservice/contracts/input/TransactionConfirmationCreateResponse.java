package ru.netology.transferservice.contracts.input;

public interface TransactionConfirmationCreateResponse extends TransactionIdentificator {
    String getCode();
}
