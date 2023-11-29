package ru.netology.transferservice.contracts.repository;

import ru.netology.transferservice.contracts.entity.AccountAllocation;

public interface AccountAllocationCreateRepository {
    long getAllocatedSum(String accountNumber);

    boolean store(AccountAllocation allocation);
}
