package ru.netology.transferservice.contracts.input;

import ru.netology.transferservice.contracts.entity.TransactionStatus;


/**
 * Базовый контракт ответов на запросы сценариев обработки транзакций
 */
public interface TransactionInputResponse extends TransactionIdentificator {
    TransactionStatus getStatus();
}
