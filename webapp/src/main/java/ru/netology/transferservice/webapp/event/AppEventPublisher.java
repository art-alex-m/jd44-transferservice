package ru.netology.transferservice.webapp.event;

import org.springframework.context.ApplicationEventPublisher;
import ru.netology.transferservice.contracts.event.TransferserviceEvent;
import ru.netology.transferservice.contracts.event.TransferserviceEventPublisher;

public class AppEventPublisher implements TransferserviceEventPublisher {
    private final ApplicationEventPublisher eventPublisher;

    public AppEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void publish(TransferserviceEvent event) {
        eventPublisher.publishEvent(event);
    }
}
