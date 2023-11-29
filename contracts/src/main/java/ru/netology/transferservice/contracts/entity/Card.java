package ru.netology.transferservice.contracts.entity;

/**
 * Банковская карта
 */
public interface Card extends AccountNumbering {
    /**
     * Номер карты (уникальный идентификатор)
     */
    String getNumber();

    String getValidTill();

    String getCvv();

    /**
     * Карта другого банка
     */
    boolean isExternal();
}
