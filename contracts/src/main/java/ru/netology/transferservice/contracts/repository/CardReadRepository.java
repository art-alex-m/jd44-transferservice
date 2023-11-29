package ru.netology.transferservice.contracts.repository;

import ru.netology.transferservice.contracts.entity.Card;

public interface CardReadRepository {
    Card findByNumber(String number);
}
