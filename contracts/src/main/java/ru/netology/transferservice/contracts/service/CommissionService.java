package ru.netology.transferservice.contracts.service;

import ru.netology.transferservice.contracts.entity.Card;
import ru.netology.transferservice.contracts.entity.Commission;

/**
 * Сервис рассчитывает комиссию за перевод средств
 */
public interface CommissionService {
    Commission compute(Card cardFrom, Card cardTo, long amount, String currency);
}
