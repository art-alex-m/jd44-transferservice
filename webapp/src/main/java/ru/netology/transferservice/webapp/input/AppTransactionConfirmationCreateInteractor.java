package ru.netology.transferservice.webapp.input;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.netology.transferservice.contracts.entity.Transaction;
import ru.netology.transferservice.contracts.event.TransactionEvent;
import ru.netology.transferservice.contracts.event.TransactionEventHandler;
import ru.netology.transferservice.contracts.event.TransactionIsCreated;
import ru.netology.transferservice.contracts.input.TransactionConfirmationCreateInput;

@Component
public class AppTransactionConfirmationCreateInteractor implements TransactionEventHandler {
    private final TransactionConfirmationCreateInput transactionConfirmationCreateInput;

    public AppTransactionConfirmationCreateInteractor(
            TransactionConfirmationCreateInput transactionConfirmationCreateInput) {
        this.transactionConfirmationCreateInput = transactionConfirmationCreateInput;
    }

    @Override
    @EventListener(classes = {TransactionIsCreated.class})
    public void handle(TransactionEvent event) {
        Transaction transaction = event.getTransaction();
        transactionConfirmationCreateInput.create(new AppTransactionConfirmationCreateRequest(transaction.getId()));
    }
}
