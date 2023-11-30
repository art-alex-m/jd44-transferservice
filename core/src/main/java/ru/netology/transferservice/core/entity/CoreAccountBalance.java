package ru.netology.transferservice.core.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.netology.transferservice.contracts.entity.AccountBalance;

@AllArgsConstructor
@Getter
public class CoreAccountBalance implements AccountBalance {
    private final String accountNumber;
    private long balance;

    @Override
    public boolean writeOff(long amount) {
        if (amount > balance || amount < 0) {
            return false;
        }
        balance -= amount;

        return true;
    }

    @Override
    public boolean add(long amount) {
        if (amount < 0) {
            return false;
        }
        balance += amount;

        return true;
    }
}
