package ru.netology.transferservice.contracts.repository;

import ru.netology.transferservice.contracts.entity.AccountAllocation;

import java.util.UUID;

public interface AccountAllocationAcceptRepository {
    AccountAllocation getByTransactionId(UUID transactionId);

    boolean delete(AccountAllocation allocation);
}
