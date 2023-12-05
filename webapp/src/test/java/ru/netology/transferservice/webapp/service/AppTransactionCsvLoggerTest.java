package ru.netology.transferservice.webapp.service;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.netology.transferservice.contracts.entity.Card;
import ru.netology.transferservice.contracts.entity.Transaction;
import ru.netology.transferservice.contracts.entity.TransactionStatusCode;
import ru.netology.transferservice.contracts.service.TransactionLogger;
import ru.netology.transferservice.core.entity.CoreCard;
import ru.netology.transferservice.core.entity.CoreCommission;
import ru.netology.transferservice.core.entity.CoreTransactionStatus;
import ru.netology.transferservice.core.factory.CoreTransactionBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AppTransactionCsvLoggerTest {

    @Test
    void log() throws IOException {
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder().build();
        UUID transactionId = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.now();
        File logFile = Files.createTempFile("appTransactionCsvLoggerTest", ".csv").toFile();
        logFile.deleteOnExit();
        Card card = new CoreCard("1234123412341235", "09/55", "123", "100100100", false);
        Transaction transaction = new CoreTransactionBuilder()
                .setId(transactionId)
                .setCardFrom(card)
                .setCardTo(card)
                .setAmount(1000)
                .setCurrency("RUB")
                .setStatus(new CoreTransactionStatus(transactionId, TransactionStatusCode.NEW, "", createdAt))
                .setCommission(new CoreCommission(23))
                .setCreatedAt(createdAt)
                .build();
        TransactionLogger sut = new AppTransactionCsvLogger(logFile);

        Executable result = () -> sut.log(transaction);

        assertDoesNotThrow(result);
        List<CSVRecord> logItems = csvFormat.parse(new FileReader(logFile)).getRecords();
        assertEquals(1, logItems.size());
        CSVRecord resultItem = logItems.get(0);
        assertEquals(11, resultItem.size());
        assertNotNull(resultItem.get(0));
        assertEquals(transactionId.toString(), resultItem.get(1));
        assertEquals(createdAt.toString(), resultItem.get(2));
        assertEquals(card.getAccountNumber(), resultItem.get(3));
        assertEquals(card.getNumber(), resultItem.get(4));
        assertEquals(card.getNumber(), resultItem.get(5));
        assertEquals("1000", resultItem.get(6));
        assertEquals("23", resultItem.get(7));
        assertEquals("1023", resultItem.get(8));
        assertEquals(TransactionStatusCode.NEW.code, resultItem.get(9));
        assertEquals(createdAt.toString(), resultItem.get(10));
    }
}