package ru.netology.transferservice.demo.repository;

import ru.netology.transferservice.contracts.entity.AccountAllocation;
import ru.netology.transferservice.contracts.repository.AccountAllocationAcceptRepository;
import ru.netology.transferservice.contracts.repository.AccountAllocationCreateRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DemoAccountAllocationRepository implements AccountAllocationCreateRepository,
        AccountAllocationAcceptRepository {

    private final Map<UUID, AccountAllocation> storage = new HashMap<>();

    @Override
    public AccountAllocation getByTransactionId(UUID transactionId) {
        return storage.get(transactionId);
    }

    @Override
    public long getAllocatedSum(String accountNumber) {
        return storage.values().stream()
                .filter(al -> al.getAccountNumber().equals(accountNumber))
                .mapToLong(AccountAllocation::getAmount)
                .sum();
    }

    @Override
    public boolean store(AccountAllocation allocation) {
        storage.put(allocation.getTransactionId(), allocation);
        return true;
    }

    @Override
    public boolean delete(AccountAllocation allocation) {
        storage.remove(allocation.getTransactionId());
        return true;
    }
}
