package ru.netology.transferservice.contracts.event;

public interface TransactionEventHandler {
    void handle(TransactionEvent event);
}
