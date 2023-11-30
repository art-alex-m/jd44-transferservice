package ru.netology.transferservice.contracts.service;

import ru.netology.transferservice.contracts.entity.Card;

public interface CardValidationService {
    boolean isValid(Card card, CardUserInput userInput);
}
