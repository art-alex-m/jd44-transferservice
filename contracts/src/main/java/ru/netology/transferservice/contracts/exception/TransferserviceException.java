package ru.netology.transferservice.contracts.exception;

public class TransferserviceException extends RuntimeException {
    private final int id;

    public TransferserviceException(int id, String message) {
        super(message);
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
