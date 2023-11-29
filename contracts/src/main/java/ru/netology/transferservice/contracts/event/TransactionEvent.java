package ru.netology.transferservice.contracts.event;

import ru.netology.transferservice.contracts.entity.Transaction;

/**
 * Базовое событие сценариев перевода денег (транзакция)
 */
public interface TransactionEvent extends TransferserviceEvent {
    Transaction getTransaction();
}
