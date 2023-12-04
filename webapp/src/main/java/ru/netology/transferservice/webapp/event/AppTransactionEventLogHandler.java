package ru.netology.transferservice.webapp.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.netology.transferservice.contracts.entity.Transaction;
import ru.netology.transferservice.contracts.event.TransactionEvent;
import ru.netology.transferservice.contracts.event.TransactionEventHandler;

import java.util.concurrent.BlockingQueue;

@Component
public class AppTransactionEventLogHandler implements TransactionEventHandler {

    private final BlockingQueue<Transaction> transactionsLogQueue;

    public AppTransactionEventLogHandler(BlockingQueue<Transaction> transactionsLogQueue) {
        this.transactionsLogQueue = transactionsLogQueue;
    }

    @Override
    @EventListener
    public void handle(TransactionEvent event) {
        try {
            transactionsLogQueue.put(event.getTransaction());
        } catch (InterruptedException ignored) {
        }
    }
}
