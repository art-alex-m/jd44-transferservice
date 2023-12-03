package ru.netology.transferservice.contracts.entity;

/**
 * Код статуса транзакции (перевода денег)
 */
public enum TransactionStatusCode {
    NEW("new"),
    SUCCESS("success"),
    CONFIRMED("confirmed"),
    ERROR("error");

    public final String code;

    TransactionStatusCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }
}
