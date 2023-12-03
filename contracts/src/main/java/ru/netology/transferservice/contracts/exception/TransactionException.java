package ru.netology.transferservice.contracts.exception;

public class TransactionException extends TransferserviceException {
    public TransactionException(int id, String message) {
        super(id, message);
    }
    public TransactionException(TransactionExceptionCode code) {
        this(code.getId(), code.getMessage());
    }
}
