package ru.netology.transferservice.contracts.factory;

import ru.netology.transferservice.contracts.entity.Card;
import ru.netology.transferservice.contracts.entity.Commission;
import ru.netology.transferservice.contracts.entity.Transaction;
import ru.netology.transferservice.contracts.entity.TransactionStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public interface TransactionBuilder {
    Transaction build();

    TransactionBuilder setId(UUID id);

    TransactionBuilder setCardFrom(Card card);

    TransactionBuilder setCardTo(Card card);

    TransactionBuilder setAmount(long amount);

    TransactionBuilder setCurrency(String currency);

    TransactionBuilder setStatus(TransactionStatus status);

    TransactionBuilder setCommission(Commission commission);

    TransactionBuilder setCreatedAt(LocalDateTime createdAt);

    TransactionBuilder toBuilder(Transaction base);
}
