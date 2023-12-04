package ru.netology.transferservice.webapp.service;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.netology.transferservice.contracts.entity.Transaction;
import ru.netology.transferservice.contracts.service.TransactionLogger;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AppTransactionLoggerWorkerTest {

    @Test
    void doWork() {
        Transaction transaction = Mockito.mock(Transaction.class);
        BlockingQueue<Transaction> transactionBlockingQueue = new ArrayBlockingQueue<>(2);
        transactionBlockingQueue.add(transaction);
        TransactionLogger logger = Mockito.mock(TransactionLogger.class);
        ArgumentCaptor<Transaction> transactionArgumentCaptor = ArgumentCaptor.forClass(Transaction.class);
        AppTransactionLoggerWorker sut = new AppTransactionLoggerWorker(logger, transactionBlockingQueue);

        sut.doWork();

        assertEquals(0, transactionBlockingQueue.size());
        Mockito.verify(logger, Mockito.times(1)).log(transactionArgumentCaptor.capture());
        assertEquals(transaction, transactionArgumentCaptor.getValue());
    }
}