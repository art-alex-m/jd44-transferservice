package ru.netology.transferservice.contracts.repository;

import ru.netology.transferservice.contracts.entity.AccountBalance;

public interface AccountBalanceAcceptRepository extends AccountBalanceRepository {
    boolean update(AccountBalance balance);
}
