package ru.netology.transferservice.core.service;

import ru.netology.transferservice.contracts.entity.Card;
import ru.netology.transferservice.contracts.entity.Commission;
import ru.netology.transferservice.contracts.service.CommissionService;
import ru.netology.transferservice.core.entity.CoreCommission;

public class CoreCommissionService implements CommissionService {
    @Override
    public Commission compute(Card cardFrom, Card cardTo, long amount, String currency) {
        return new CoreCommission((long) (amount * 0.01));
    }
}
