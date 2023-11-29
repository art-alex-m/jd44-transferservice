package ru.netology.transferservice.contracts.repository;

import ru.netology.transferservice.contracts.entity.AccountBalance;

public interface AccountBalanceRepository {
    AccountBalance findByAccountNumber(String accountNUmber);
}
