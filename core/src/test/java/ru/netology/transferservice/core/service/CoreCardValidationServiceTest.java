package ru.netology.transferservice.core.service;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.netology.transferservice.contracts.entity.Card;
import ru.netology.transferservice.contracts.service.CardUserInput;
import ru.netology.transferservice.contracts.service.CardValidationService;
import ru.netology.transferservice.core.entity.CoreCard;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CoreCardValidationServiceTest {

    public static Stream<Arguments> isValid() {
        Card card = new CoreCard("1234 1234 1234 12345", "09/55", "123", "100100100", false);
        Card oldCard = new CoreCard("1234 1234 1234 12345", "09/10", "123", "100100100", false);

        return Stream.of(
                Arguments.of(card, new CoreCardUserInput("123", "09/55"), true),
                Arguments.of(card, new CoreCardUserInput("123", "09/54"), false),
                Arguments.of(card, new CoreCardUserInput("123", "09/56"), false),
                Arguments.of(card, new CoreCardUserInput("123", "10/55"), false),
                Arguments.of(card, new CoreCardUserInput("321", "09/55"), false),
                Arguments.of(card, new CoreCardUserInput("321", "09/50"), false),
                Arguments.of(oldCard, new CoreCardUserInput("123", "09/10"), false)
        );
    }

    @ParameterizedTest
    @MethodSource
    void isValid(Card card, CardUserInput userInput, boolean expected) {
        CardValidationService sut = new CoreCardValidationService();

        boolean result = sut.isValid(card, userInput);

        assertEquals(expected, result);
    }
}
