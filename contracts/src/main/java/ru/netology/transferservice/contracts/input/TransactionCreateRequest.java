package ru.netology.transferservice.contracts.input;

/**
 * Запрос на создание транзакции на перевод денег
 */
public interface TransactionCreateRequest {
    String getCardFromNumber();

    String getCardFromValidTill();

    String geCardFromCvv();

    String getCardToNumber();

    String getCurrency();

    long getAmount();
}
