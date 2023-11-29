package ru.netology.transferservice.contracts.service;

import ru.netology.transferservice.contracts.entity.AccountAllocation;

import java.util.UUID;

public interface AccountAllocationService {
    /**
     * Резервирует запрошенную сумму на счете
     *
     * @param transactionId Идентификатор транзакции
     * @param accountNumber Идентификатор счета
     * @param amount        Требуемая сумма
     * @return В случае успеха - объект резервирования, null - резервирование не удалось
     */
    AccountAllocation allocate(UUID transactionId, String accountNumber, long amount);
}
