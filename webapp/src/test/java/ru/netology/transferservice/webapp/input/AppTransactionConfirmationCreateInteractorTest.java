package ru.netology.transferservice.webapp.input;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.netology.transferservice.contracts.entity.Transaction;
import ru.netology.transferservice.contracts.event.TransactionEvent;
import ru.netology.transferservice.contracts.event.TransactionIsCreated;
import ru.netology.transferservice.contracts.input.TransactionConfirmationCreateInput;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AppTransactionConfirmationCreateInteractorTest {

    @Test
    void handle() {
        UUID transactionId = UUID.randomUUID();
        Transaction transaction = Mockito.mock(Transaction.class);
        Mockito.when(transaction.getId()).thenReturn(transactionId);
        TransactionEvent transactionEvent = new TransactionIsCreated(transaction);
        TransactionConfirmationCreateInput confirmationCreateInput = Mockito.mock(
                TransactionConfirmationCreateInput.class);
        ArgumentCaptor<AppTransactionConfirmationCreateRequest> requestArgumentCaptor = ArgumentCaptor.forClass(
                AppTransactionConfirmationCreateRequest.class);
        AppTransactionConfirmationCreateInteractor sut = new AppTransactionConfirmationCreateInteractor(
                confirmationCreateInput);

        sut.handle(transactionEvent);

        Mockito.verify(confirmationCreateInput, Mockito.times(1)).create(requestArgumentCaptor.capture());
        assertEquals(transactionId, requestArgumentCaptor.getValue().getTransactionId());
    }
}