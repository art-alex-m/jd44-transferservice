package ru.netology.transferservice.core.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import ru.netology.transferservice.contracts.entity.AccountAllocation;
import ru.netology.transferservice.contracts.entity.AccountBalance;
import ru.netology.transferservice.contracts.repository.AccountAllocationCreateRepository;
import ru.netology.transferservice.contracts.repository.AccountBalanceRepository;
import ru.netology.transferservice.contracts.service.AccountAllocationService;
import ru.netology.transferservice.core.entity.CoreAccountAllocation;
import ru.netology.transferservice.core.entity.CoreAccountBalance;

import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CoreAccountAllocationServiceTest {

    public static Stream<Arguments> allocate() {
        UUID transactionId = UUID.randomUUID();
        String accountNumber = "1234123412341234";
        AccountBalance balance = new CoreAccountBalance(accountNumber, 1000);

        return Stream.of(
                Arguments.of(780, 200, false, transactionId, accountNumber, null, null),
                Arguments.of(780, 200, true, transactionId, accountNumber, balance,
                        new CoreAccountAllocation(transactionId, accountNumber, 780)),
                Arguments.of(780, 200, false, transactionId, accountNumber, balance, null),
                Arguments.of(900, 200, false, transactionId, accountNumber, balance, null),
                Arguments.of(780, 300, false, transactionId, accountNumber, balance, null),
                Arguments.of(780, 0, false, transactionId, accountNumber, new CoreAccountBalance(accountNumber, 100),
                        null)
        );
    }

    @ParameterizedTest
    @MethodSource
    void allocate(long amount, long totalAllocated, boolean storeResult, UUID transactionId, String accountNumber,
            AccountBalance balance, AccountAllocation expectedAllocation) {
        AccountBalanceRepository balanceRepository = Mockito.mock(AccountBalanceRepository.class);
        Mockito.when(balanceRepository.findByAccountNumber(accountNumber)).thenReturn(balance);
        AccountAllocationCreateRepository allocationRepository = Mockito.mock(AccountAllocationCreateRepository.class);
        Mockito.when(allocationRepository.getAllocatedSum(accountNumber)).thenReturn(totalAllocated);
        Mockito.when(allocationRepository.store(Mockito.any(AccountAllocation.class))).thenReturn(storeResult);
        AccountAllocationService sut = new CoreAccountAllocationService(balanceRepository, allocationRepository);

        AccountAllocation result = sut.allocate(transactionId, accountNumber, amount);

        assertEquals(expectedAllocation, result);
    }

    @Test
    void delete() {
        AccountAllocation allocation = new CoreAccountAllocation(UUID.randomUUID(), "123", 1);
        AccountBalanceRepository balanceRepository = Mockito.mock(AccountBalanceRepository.class);
        AccountAllocationCreateRepository allocationRepository = Mockito.mock(AccountAllocationCreateRepository.class);
        Mockito.when(allocationRepository.delete(allocation)).thenReturn(true);
        AccountAllocationService sut = new CoreAccountAllocationService(balanceRepository, allocationRepository);

        boolean result = sut.delete(allocation);

        assertTrue(result);
        Mockito.verify(allocationRepository, Mockito.times(1)).delete(allocation);
    }
}
