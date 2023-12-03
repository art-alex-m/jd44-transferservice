package ru.netology.transferservice.core.service;

import ru.netology.transferservice.contracts.entity.Card;
import ru.netology.transferservice.contracts.repository.CardReadRepository;
import ru.netology.transferservice.contracts.service.CardReadService;

import java.util.LinkedList;
import java.util.List;

public class CoreCardReadService implements CardReadService {
    private final List<CardReadRepository> repositories = new LinkedList<>();

    @Override
    public Card getCardByNumber(String number) {
        for (CardReadRepository repo : repositories) {
            Card card = repo.findByNumber(number);
            if (card != null) {
                return card;
            }
        }

        return null;
    }

    public CoreCardReadService addRepository(CardReadRepository cardReadRepository) {
        repositories.add(cardReadRepository);
        return this;
    }

    public List<CardReadRepository> getRepositories() {
        return repositories;
    }
}
