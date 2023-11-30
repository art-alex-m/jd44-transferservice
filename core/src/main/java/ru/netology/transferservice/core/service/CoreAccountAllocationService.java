package ru.netology.transferservice.core.service;

import ru.netology.transferservice.contracts.entity.AccountAllocation;
import ru.netology.transferservice.contracts.entity.AccountBalance;
import ru.netology.transferservice.contracts.repository.AccountAllocationCreateRepository;
import ru.netology.transferservice.contracts.repository.AccountBalanceRepository;
import ru.netology.transferservice.contracts.service.AccountAllocationService;
import ru.netology.transferservice.core.entity.CoreAccountAllocation;

import java.util.UUID;

public class CoreAccountAllocationService implements AccountAllocationService {
    private final AccountBalanceRepository balanceRepository;
    private final AccountAllocationCreateRepository allocationRepository;

    public CoreAccountAllocationService(AccountBalanceRepository balanceRepository,
            AccountAllocationCreateRepository allocationRepository) {
        this.balanceRepository = balanceRepository;
        this.allocationRepository = allocationRepository;
    }

    @Override
    public AccountAllocation allocate(UUID transactionId, String accountNumber, long amount) {
        AccountBalance balance = balanceRepository.findByAccountNumber(accountNumber);
        if (balance == null) {
            return null;
        }
        long totalAllocated = allocationRepository.getAllocatedSum(accountNumber);
        long tryAllocate = balance.getBalance() - totalAllocated - amount;
        if (tryAllocate < 0) {
            return null;
        }
        AccountAllocation allocation = new CoreAccountAllocation(transactionId, accountNumber, amount);
        if (!allocationRepository.store(allocation)) {
            return null;
        }
        return allocation;
    }

    @Override
    public boolean delete(AccountAllocation allocation) {
        return allocationRepository.delete(allocation);
    }
}
