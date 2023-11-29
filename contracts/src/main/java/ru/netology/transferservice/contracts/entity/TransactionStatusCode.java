package ru.netology.transferservice.contracts.entity;

/**
 * Код статуса транзакции (перевода денег)
 */
public enum TransactionStatusCode {
    NEW("new"),
    SUCCESS("success"),
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
