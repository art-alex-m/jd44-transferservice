package ru.netology.transferservice.core.factory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.transferservice.contracts.entity.TransactionStatus;
import ru.netology.transferservice.contracts.entity.TransactionStatusCode;
import ru.netology.transferservice.contracts.factory.TransactionStatusFactory;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CoreTransactionStatusFactoryTest {

    TransactionStatusFactory sut;

    @BeforeEach
    void setUp() {
        sut = new CoreTransactionStatusFactory();
    }

    @Test
    void create() {
        TransactionStatusCode code = TransactionStatusCode.SUCCESS;
        String message = "Some status code";

        TransactionStatus result = sut.create(code, message);

        assertNotNull(result.getId());
        assertEquals(code, result.getCode());
        assertEquals(message, result.getMessage());
        assertNotNull(result.getCreatedAt());
        assertTrue(LocalDateTime.now().isAfter(result.getCreatedAt()));
    }

    @Test
    void createOnlyWithStatusCode() {
        TransactionStatusCode code = TransactionStatusCode.CONFIRMED;

        TransactionStatus result = sut.create(code);

        assertNotNull(result.getId());
        assertEquals(code, result.getCode());
        assertNull(result.getMessage());
        assertNotNull(result.getCreatedAt());
        assertTrue(LocalDateTime.now().isAfter(result.getCreatedAt()));
    }
}