package ru.netology.transferservice.contracts.exception;

public class TransactionException extends TransferserviceException {
    public TransactionException(TransactionExceptionCode code) {
        super(code.getId(), code.getMessage());
    }
}
