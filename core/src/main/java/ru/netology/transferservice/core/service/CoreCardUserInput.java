package ru.netology.transferservice.core.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.netology.transferservice.contracts.service.CardUserInput;

@AllArgsConstructor
@Getter
public class CoreCardUserInput implements CardUserInput {
    private final String cvv;
    private final String validTill;
}
