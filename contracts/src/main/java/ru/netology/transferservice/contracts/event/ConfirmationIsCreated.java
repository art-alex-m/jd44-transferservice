package ru.netology.transferservice.contracts.event;

import ru.netology.transferservice.contracts.entity.Confirmation;

import java.time.LocalDateTime;

public class ConfirmationIsCreated implements TransferserviceEvent {
    private final Confirmation confirmation;
    private final LocalDateTime createdAt;

    public ConfirmationIsCreated(Confirmation confirmation) {
        this.confirmation = confirmation;
        this.createdAt = LocalDateTime.now();
    }

    @Override
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Confirmation getConfirmation() {
        return confirmation;
    }
}
