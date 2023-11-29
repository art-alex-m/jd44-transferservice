package ru.netology.transferservice.contracts.event;

import java.time.LocalDateTime;

/**
 * Базовое доменное событие
 */
public interface TransferserviceEvent {
    LocalDateTime getCreatedAt();
}
