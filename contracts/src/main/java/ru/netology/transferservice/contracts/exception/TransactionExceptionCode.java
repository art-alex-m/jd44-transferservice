package ru.netology.transferservice.contracts.exception;

public enum TransactionExceptionCode {
    CARD_FROM_NOT_FOUND(100, "transaction.exception.cardFromNotFound"),
    CARD_TO_NOT_FOUND(101, "transaction.exception.cardToNotFound"),
    CARD_FROM_NOT_VALID(102, "transaction.exception.cardFromNotValid"),
    CANNOT_ALLOCATE_SUM(103, "transaction.exception.cannotAllocateSum"),
    CANNOT_CREATE_CONFIRMATION(104, "transaction.exception.cannotCreateConfirmation"),
    CANNOT_CREATE_TRANSACTION(105, "transaction.exception.cannotCreateTransaction"),
    TRANSACTION_NOT_FOUND(106, "transaction.exception.transactionNotFound"),
    CONFIRMATION_NOT_FOUND(107, "transaction.exception.confirmationNotFound"),
    CONFIRMATION_CODE_INVALID(108, "transaction.exception.confirmationCodeInvalid"),
    CANNOT_UPDATE_TRANSACTION(109, "transaction.exception.cannotUpdateTransaction");

    private final int id;
    private final String message;

    TransactionExceptionCode(int id, String message) {
        this.id = id;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }
}
