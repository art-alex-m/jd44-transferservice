package ru.netology.transferservice.webapp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import ru.netology.transferservice.contracts.repository.*;
import ru.netology.transferservice.demo.repository.*;

import java.util.List;

@Configuration
@Order(100)
public class TransferserviceWebDemoConfiguration {
    /**
     * Implements
     *
     * @see ConfirmationCreateRepository
     * @see ConfirmationConfirmRepository
     */
    @Bean
    public DemoConfirmationRepository appConfirmationRepository() {
        return new DemoConfirmationRepository();
    }

    /**
     * Implements
     *
     * @see TransactionCreateRepository
     * @see TransactionConfirmRepository
     * @see TransactionAcceptRepository
     */
    @Bean
    public DemoTransactionRepository appTransactionRepository() {
        return new DemoTransactionRepository();
    }

    /**
     * Implements
     *
     * @see AccountBalanceRepository
     * @see AccountBalanceAcceptRepository
     */
    @Bean
    public DemoAccountBalanceRepository appAccountBalanceRepository() {
        return new DemoAccountBalanceRepository();
    }

    /**
     * Implements
     *
     * @see AccountAllocationCreateRepository
     * @see AccountAllocationAcceptRepository
     */
    @Bean
    public DemoAccountAllocationRepository appAccountAllocationRepository() {
        return new DemoAccountAllocationRepository();
    }

    @Bean
    public List<CardReadRepository> cardReadRepositoryList() {
        return List.of(new DemoCardReadRepository());
    }
}
