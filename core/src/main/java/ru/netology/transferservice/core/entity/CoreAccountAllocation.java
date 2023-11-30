package ru.netology.transferservice.core.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.netology.transferservice.contracts.entity.AccountAllocation;

import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class CoreAccountAllocation implements AccountAllocation {
    private final UUID id;
    private final UUID transactionId;
    private final String accountNumber;
    private final long amount;

    public CoreAccountAllocation(UUID transactionId, String accountNumber, long amount) {
        this(UUID.randomUUID(), transactionId, accountNumber, amount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CoreAccountAllocation that)) return false;
        return amount == that.amount &&
                Objects.equals(transactionId, that.transactionId) &&
                Objects.equals(accountNumber, that.accountNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId, accountNumber, amount);
    }
}
