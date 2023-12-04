package ru.netology.transferservice.webapp.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.netology.transferservice.contracts.entity.Card;
import ru.netology.transferservice.contracts.entity.Transaction;
import ru.netology.transferservice.contracts.entity.TransactionStatusCode;
import ru.netology.transferservice.core.entity.CoreCard;
import ru.netology.transferservice.core.entity.CoreCommission;
import ru.netology.transferservice.core.entity.CoreTransactionStatus;
import ru.netology.transferservice.core.factory.CoreTransactionBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AppTransactionCsvLoggerTest {

    @Test
    void log() throws IOException {
        UUID id = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.now();
        Path outFile = Files.createTempFile("appTransactionCsvLoggerTest", ".csv");
        File logFile = outFile.toFile();
        logFile.deleteOnExit();
        long initialSize = Files.size(outFile);
        Card card = new CoreCard("1234 1234 1234 12345", "09/55", "123", "100100100", false);
        Transaction transaction = new CoreTransactionBuilder()
                .setId(id)
                .setCardFrom(card)
                .setCardTo(card)
                .setAmount(1000)
                .setCurrency("RUB")
                .setStatus(new CoreTransactionStatus(id, TransactionStatusCode.NEW, "", createdAt))
                .setCommission(new CoreCommission(23))
                .setCreatedAt(createdAt)
                .build();
        AppTransactionCsvLogger sut = new AppTransactionCsvLogger(logFile);

        Executable result = () -> sut.log(transaction);

        assertDoesNotThrow(result);
        assertTrue(initialSize < Files.size(outFile));
    }
}