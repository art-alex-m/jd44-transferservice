package ru.netology.transferservice.core.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import ru.netology.transferservice.contracts.entity.Card;
import ru.netology.transferservice.contracts.repository.CardReadRepository;
import ru.netology.transferservice.contracts.service.CardReadService;
import ru.netology.transferservice.core.entity.CoreCard;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CoreCardReadServiceTest {

    public static Stream<Arguments> getCardByNumber() {
        return Stream.of(
                Arguments.of("1234", new CoreCard("1234", "09/55", "123", "1234", false)),
                Arguments.of("4231", null)
        );
    }

    @ParameterizedTest
    @MethodSource
    void getCardByNumber(String accountNumber, Card card) {
        CardReadRepository repo1 = Mockito.mock(CardReadRepository.class);
        Mockito.when(repo1.findByNumber(accountNumber)).thenReturn(card);
        CardReadRepository repo2 = Mockito.mock(CardReadRepository.class);
        CardReadService sut = new CoreCardReadService().addRepository(repo2).addRepository(repo1);

        Card result = sut.getCardByNumber(accountNumber);

        assertEquals(card, result);
        Mockito.verify(repo1, Mockito.times(1)).findByNumber(accountNumber);
        Mockito.verifyNoMoreInteractions(repo1);
        Mockito.verify(repo2, Mockito.times(1)).findByNumber(accountNumber);
        Mockito.verifyNoMoreInteractions(repo2);
    }

    @Test
    void addRepository() {
        CardReadRepository repo1 = Mockito.mock(CardReadRepository.class);
        CardReadRepository repo2 = Mockito.mock(CardReadRepository.class);
        CoreCardReadService sut = new CoreCardReadService().addRepository(repo1).addRepository(repo2);

        List<CardReadRepository> result = sut.getRepositories();

        assertEquals(2, result.size());
        assertArrayEquals(List.of(repo1, repo2).toArray(), result.toArray());
    }
}
