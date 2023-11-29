package ru.netology.transferservice.contracts.event;

public interface TransferserviceEventPublisher {
    void publish(TransferserviceEvent event);
}
