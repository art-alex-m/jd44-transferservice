package ru.netology.transferservice.webapp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.netology.transferservice.contracts.entity.Transaction;
import ru.netology.transferservice.contracts.service.TransactionLogger;

import java.util.concurrent.BlockingQueue;

@Slf4j
@Component
public class AppTransactionLoggerWorker implements Runnable {
    private final TransactionLogger transactionLogger;
    private final BlockingQueue<Transaction> transactionLogQueue;

    public AppTransactionLoggerWorker(TransactionLogger transactionLogger,
            BlockingQueue<Transaction> transactionLogQueue) {
        this.transactionLogger = transactionLogger;
        this.transactionLogQueue = transactionLogQueue;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            doWork();
        }
    }

    public void doWork() {
        try {
            Transaction transaction = transactionLogQueue.take();
            transactionLogger.log(transaction);
        } catch (Exception ex) {
            log.error(ex.toString());
        }
    }
}
