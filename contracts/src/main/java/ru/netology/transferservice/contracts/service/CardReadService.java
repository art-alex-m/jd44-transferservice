package ru.netology.transferservice.contracts.service;

import ru.netology.transferservice.contracts.entity.Card;

/**
 * Сервис находит сведение о банковской карте
 */
public interface CardReadService {
    Card getCardByNumber(String number);
}
