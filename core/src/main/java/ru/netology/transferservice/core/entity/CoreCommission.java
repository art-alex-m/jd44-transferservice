package ru.netology.transferservice.core.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.netology.transferservice.contracts.entity.Commission;

@AllArgsConstructor
@Getter
public class CoreCommission implements Commission {
    private final long amount;
}
