package ru.netology.transferservice.core.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.netology.transferservice.contracts.entity.Card;

@AllArgsConstructor
@Getter
public class CoreCard implements Card {
    private final String number;
    private final String validTill;
    private final String cvv;
    private final String accountNumber;
    private final boolean isExternal;
}
