package ru.netology.transferservice.webapp.event;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.netology.transferservice.contracts.entity.Transaction;
import ru.netology.transferservice.contracts.event.TransactionEvent;
import ru.netology.transferservice.contracts.event.TransactionEventHandler;
import ru.netology.transferservice.contracts.event.TransactionIsCreated;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AppTransactionEventLogHandlerTest {

    @Test
    void handle() {
        Transaction transaction = Mockito.mock(Transaction.class);
        TransactionEvent transactionEvent = new TransactionIsCreated(transaction);
        BlockingQueue<Transaction> transactionBlockingQueue = new ArrayBlockingQueue<>(2);
        TransactionEventHandler sut = new AppTransactionEventLogHandler(transactionBlockingQueue);

        sut.handle(transactionEvent);

        assertEquals(1, transactionBlockingQueue.size());
        assertEquals(transaction, transactionBlockingQueue.poll());
    }
}