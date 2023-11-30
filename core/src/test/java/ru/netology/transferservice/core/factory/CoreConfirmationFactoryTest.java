package ru.netology.transferservice.core.factory;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.netology.transferservice.contracts.entity.Confirmation;
import ru.netology.transferservice.contracts.factory.ConfirmationFactory;
import ru.netology.transferservice.core.entity.CoreConfirmation;

import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CoreConfirmationFactoryTest {

    private static final UUID transactionId = UUID.randomUUID();

    public static Stream<Arguments> create() {
        String template = "555555";
        return Stream.of(
                Arguments.of(3, template, new CoreConfirmation("555", transactionId)),
                Arguments.of(4, template, new CoreConfirmation("5555", transactionId)),
                Arguments.of(5, template, new CoreConfirmation("55555", transactionId))
        );
    }

    @ParameterizedTest
    @MethodSource
    void create(int size, String template, Confirmation expected) {
        ConfirmationFactory sut = new CoreConfirmationFactory(size, template);

        Confirmation result = sut.create(transactionId);

        assertEquals(expected.getCode(), result.getCode());
        assertEquals(expected.getTransactionId(), result.getTransactionId());
    }
}