package ru.netology.transferservice.demo.repository;

import ru.netology.transferservice.contracts.entity.AccountBalance;
import ru.netology.transferservice.contracts.repository.AccountBalanceAcceptRepository;
import ru.netology.transferservice.contracts.repository.AccountBalanceRepository;
import ru.netology.transferservice.core.entity.CoreAccountBalance;

import java.util.HashMap;
import java.util.Map;

public class DemoAccountBalanceRepository implements AccountBalanceRepository, AccountBalanceAcceptRepository {
    private final Map<String, AccountBalance> storage = new HashMap<>() {{
        put("12345", new CoreAccountBalance("12345", 100000));
    }};

    @Override
    public AccountBalance findByAccountNumber(String accountNumber) {
        return storage.get(accountNumber);
    }

    @Override
    public boolean update(AccountBalance balance) {
        storage.replace(balance.getAccountNumber(), balance);
        return true;
    }
}
