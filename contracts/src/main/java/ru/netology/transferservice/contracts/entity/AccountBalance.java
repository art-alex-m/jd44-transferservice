package ru.netology.transferservice.contracts.entity;

/**
 * Баланс банковского счета
 */
public interface AccountBalance extends AccountNumbering {
    long getBalance();

    /**
     * Списать сумму со счета
     *
     * @param amount Списываемая сумма
     * @return Результат списания
     */
    boolean writeOff(long amount);

    /**
     * Зачислить сумму на счет
     *
     * @param amount Зачисляемая сумма
     * @return Результат зачисления
     */
    boolean add(long amount);
}
