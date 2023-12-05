package ru.netology.transferservice.webapp.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import ru.netology.transferservice.contracts.entity.Transaction;
import ru.netology.transferservice.contracts.service.TransactionLogger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.stream.Stream;

public class AppTransactionCsvLogger implements TransactionLogger {
    private final CSVPrinter csvPrinter;

    public AppTransactionCsvLogger(File outFile) {
        try {
            CSVFormat.Builder csvFormat = CSVFormat.DEFAULT.builder();
            if (!outFile.exists()) {
                csvFormat.setHeader(Item.Fields.class);
            }
            csvPrinter = new CSVPrinter(new FileWriter(outFile, true), csvFormat.build());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void log(Transaction transaction) {
        try {
            csvPrinter.printRecord(new Item(transaction).stream());
            csvPrinter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FieldNameConstants(asEnum = true)
    @AllArgsConstructor
    @Getter
    public static class Item {
        String loggedAt;
        String transactionId;
        String transactionCreatedAt;
        String accountCardFrom;
        String cardFromNumber;
        String cardToNumber;
        long amount;
        long commission;
        long amountWithCommission;
        String status;
        String statusCreatedAt;

        Item(Transaction transaction) {
            this(
                    LocalDateTime.now().toString(),
                    transaction.getId().toString(),
                    transaction.getCreatedAt().toString(),
                    transaction.getCardFrom().getAccountNumber(),
                    transaction.getCardFrom().getNumber(),
                    transaction.getCardTo().getNumber(),
                    transaction.getAmount(),
                    transaction.getCommission().getAmount(),
                    transaction.getAmountWithCommission(),
                    transaction.getStatus().getCode().code,
                    transaction.getStatus().getCreatedAt().toString()
            );
        }

        Stream<Object> stream() {
            return Stream.of(loggedAt, transactionId, transactionCreatedAt, accountCardFrom, cardFromNumber,
                    cardToNumber, amount, commission, amountWithCommission, status, statusCreatedAt);
        }
    }
}
