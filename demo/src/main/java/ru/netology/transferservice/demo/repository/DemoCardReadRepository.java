package ru.netology.transferservice.demo.repository;

import ru.netology.transferservice.contracts.entity.Card;
import ru.netology.transferservice.contracts.repository.CardReadRepository;
import ru.netology.transferservice.core.entity.CoreCard;

import java.util.HashMap;
import java.util.Map;

public class DemoCardReadRepository implements CardReadRepository {

    private final Map<String, Card> cardMap = new HashMap<>() {{
        put("1234123412341234", new CoreCard("1234123412341234", "09/55", "123", "12345", false));
    }};

    @Override
    public Card findByNumber(String number) {
        return cardMap.get(number);
    }
}
