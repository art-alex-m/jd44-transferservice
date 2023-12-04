package ru.netology.transferservice.webapp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.annotation.ApplicationScope;
import ru.netology.transferservice.contracts.entity.Transaction;
import ru.netology.transferservice.contracts.event.TransferserviceEventPublisher;
import ru.netology.transferservice.contracts.factory.ConfirmationFactory;
import ru.netology.transferservice.contracts.factory.TransactionStatusFactory;
import ru.netology.transferservice.contracts.input.TransactionConfirmationCreateInput;
import ru.netology.transferservice.contracts.input.TransactionConfirmationInput;
import ru.netology.transferservice.contracts.input.TransactionCreateInput;
import ru.netology.transferservice.contracts.repository.*;
import ru.netology.transferservice.contracts.service.*;
import ru.netology.transferservice.core.factory.CoreConfirmationFactory;
import ru.netology.transferservice.core.factory.CoreTransactionStatusFactory;
import ru.netology.transferservice.core.input.CoreTransactionConfirmationCreateInteractor;
import ru.netology.transferservice.core.input.CoreTransactionConfirmationInteractor;
import ru.netology.transferservice.core.input.CoreTransactionCreateInteractor;
import ru.netology.transferservice.core.service.CoreAccountAllocationService;
import ru.netology.transferservice.core.service.CoreCardReadService;
import ru.netology.transferservice.core.service.CoreCardValidationService;
import ru.netology.transferservice.core.service.CoreCommissionService;
import ru.netology.transferservice.webapp.event.AppEventPublisher;
import ru.netology.transferservice.webapp.service.AppTransactionCsvLogger;
import ru.netology.transferservice.webapp.service.AppTransactionLoggerWorker;

import java.io.File;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Configuration
@Order(1000)
public class TransferserviceWebBaseConfiguration {

    @Bean
    public TransactionCreateInput transactionCreateInput(CardReadService cardReadService,
            CommissionService commissionService, AccountAllocationService allocationService,
            TransferserviceEventPublisher eventPublisher, TransactionCreateRepository appTransactionRepository,
            TransactionStatusFactory transactionStatusFactory, CardValidationService cardValidationService
    ) {
        return new CoreTransactionCreateInteractor(
                cardReadService, commissionService, allocationService, eventPublisher, appTransactionRepository,
                transactionStatusFactory, cardValidationService
        );
    }

    @Bean
    public TransactionConfirmationCreateInput transactionConfirmationCreateInput(
            ConfirmationFactory confirmationFactory,
            ConfirmationCreateRepository appConfirmationRepository,
            TransferserviceEventPublisher eventPublisher
    ) {
        return new CoreTransactionConfirmationCreateInteractor(confirmationFactory, appConfirmationRepository,
                eventPublisher);
    }

    @Bean
    public TransactionConfirmationInput transactionConfirmationInput(
            TransactionConfirmRepository appTransactionRepository,
            ConfirmationConfirmRepository appConfirmationRepository,
            TransactionStatusFactory transactionStatusFactory,
            TransferserviceEventPublisher eventPublisher
    ) {
        return new CoreTransactionConfirmationInteractor(appTransactionRepository, appConfirmationRepository,
                transactionStatusFactory, eventPublisher);
    }

    @Bean
    public TransferserviceEventPublisher transferserviceEventPublisher(ApplicationEventPublisher eventPublisher) {
        return new AppEventPublisher(eventPublisher);
    }

    @Bean
    public AccountAllocationService accountAllocationService(
            AccountBalanceRepository appAccountBalanceRepository,
            AccountAllocationCreateRepository appAccountAllocationRepository
    ) {
        return new CoreAccountAllocationService(appAccountBalanceRepository, appAccountAllocationRepository);
    }

    @Bean
    public CardReadService cardReadService(List<CardReadRepository> cardReadRepositoryList) {
        CoreCardReadService cardReadService = new CoreCardReadService();
        cardReadRepositoryList.forEach(cardReadService::addRepository);
        return cardReadService;
    }

    @Bean
    public ConfirmationFactory confirmationFactory(
            @Value("${transferservice.transaction.confirmation.size}") int size,
            @Value("${transferservice.transaction.confirmation.template}") String template) {
        return new CoreConfirmationFactory(size, template);
    }

    @Bean
    public TransactionStatusFactory transactionStatusFactory() {
        return new CoreTransactionStatusFactory();
    }

    @Bean
    public CardValidationService cardValidationService() {
        return new CoreCardValidationService();
    }

    @Bean
    public CommissionService commissionService() {
        return new CoreCommissionService();
    }

    @Bean
    public TransactionLogger transactionLogger(
            @Value("${transferservice.transaction.log.fileName}") String logFileName) {
        return new AppTransactionCsvLogger(new File(logFileName));
    }

    @Bean
    public BlockingQueue<Transaction> transactionsLogQueue(
            @Value("${transferservice.transaction.logQueue.capacity:10000}") int capacity) {
        return new LinkedBlockingQueue<>(capacity);
    }

    @Bean
    @ApplicationScope
    public CommandLineRunner appTransactionLoggerWorkerStarter(AppTransactionLoggerWorker appTransactionLoggerWorker) {
        return args -> new Thread(appTransactionLoggerWorker, "transaction-log").start();
    }
}
