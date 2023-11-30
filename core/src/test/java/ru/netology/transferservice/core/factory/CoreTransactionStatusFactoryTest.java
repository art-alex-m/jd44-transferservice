package ru.netology.transferservice.core.factory;

import org.junit.jupiter.api.Test;
import ru.netology.transferservice.contracts.entity.TransactionStatus;
import ru.netology.transferservice.contracts.entity.TransactionStatusCode;
import ru.netology.transferservice.contracts.factory.TransactionStatusFactory;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CoreTransactionStatusFactoryTest {

    @Test
    void create() {
        TransactionStatusCode code = TransactionStatusCode.SUCCESS;
        String message = "Some status code";
        TransactionStatusFactory sut = new CoreTransactionStatusFactory();

        TransactionStatus result = sut.create(code, message);

        assertNotNull(result.getId());
        assertEquals(code, result.getCode());
        assertEquals(message, result.getMessage());
        assertNotNull(result.getCreatedAt());
        assertTrue(LocalDateTime.now().isAfter(result.getCreatedAt()));
    }
}