package ru.netology.transferservice.core.service;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import ru.netology.transferservice.contracts.entity.Card;
import ru.netology.transferservice.contracts.entity.Commission;
import ru.netology.transferservice.contracts.service.CommissionService;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CoreCommissionServiceTest {

    public static Stream<Arguments> compute() {
        return Stream.of(
                Arguments.of(100, 1),
                Arguments.of(111, 1),
                Arguments.of(777, 7)
        );
    }

    @ParameterizedTest
    @MethodSource
    void compute(long amount, long expectedCommission) {
        Card card = Mockito.mock(Card.class);
        String currency = "RUB";
        CommissionService sut = new CoreCommissionService();

        Commission result = sut.compute(card, card, amount, currency);

        assertEquals(expectedCommission, result.getAmount());
    }
}