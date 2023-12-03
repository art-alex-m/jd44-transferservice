package ru.netology.transferservice.core.input;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.netology.transferservice.contracts.entity.Confirmation;
import ru.netology.transferservice.contracts.entity.Transaction;
import ru.netology.transferservice.contracts.entity.TransactionStatusCode;
import ru.netology.transferservice.contracts.event.TransactionIsConfirmed;
import ru.netology.transferservice.contracts.event.TransferserviceEventPublisher;
import ru.netology.transferservice.contracts.exception.TransactionException;
import ru.netology.transferservice.contracts.exception.TransactionExceptionCode;
import ru.netology.transferservice.contracts.factory.TransactionStatusFactory;
import ru.netology.transferservice.contracts.input.TransactionConfirmationInput;
import ru.netology.transferservice.contracts.input.TransactionConfirmationRequest;
import ru.netology.transferservice.contracts.input.TransactionConfirmationResponse;
import ru.netology.transferservice.contracts.repository.ConfirmationConfirmRepository;
import ru.netology.transferservice.contracts.repository.TransactionConfirmRepository;
import ru.netology.transferservice.core.entity.CoreConfirmation;
import ru.netology.transferservice.core.factory.CoreTransactionStatusFactory;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CoreTransactionConfirmationInteractorTest {

    @Test
    void whenTransactionNotFound_thenThrowTransactionException() {
        UUID transactionId = UUID.randomUUID();
        TransactionConfirmationRequest request = Mockito.mock(TransactionConfirmationRequest.class);
        Mockito.when(request.getTransactionId()).thenReturn(transactionId);
        TransactionConfirmRepository transactionConfirmRepository = Mockito.mock(TransactionConfirmRepository.class);
        Mockito.when(transactionConfirmRepository.getById(transactionId)).thenReturn(null);
        ConfirmationConfirmRepository confirmRepository = Mockito.mock(ConfirmationConfirmRepository.class);
        TransactionStatusFactory statusFactory = new CoreTransactionStatusFactory();
        TransferserviceEventPublisher eventPublisher = Mockito.mock(TransferserviceEventPublisher.class);
        TransactionConfirmationInput sut = new CoreTransactionConfirmationInteractor(transactionConfirmRepository,
                confirmRepository, statusFactory, eventPublisher);

        TransactionException result = null;
        try {
            sut.confirm(request);
        } catch (TransactionException ex) {
            result = ex;
        }

        assertNotNull(result);
        assertEquals(TransactionExceptionCode.TRANSACTION_NOT_FOUND.getId(), result.getId());
        assertEquals(TransactionExceptionCode.TRANSACTION_NOT_FOUND.getMessage(), result.getMessage());
        Mockito.verify(transactionConfirmRepository, Mockito.times(1)).getById(Mockito.any(UUID.class));
        Mockito.verifyNoMoreInteractions(transactionConfirmRepository);
        Mockito.verifyNoInteractions(confirmRepository);
        Mockito.verifyNoInteractions(eventPublisher);
    }

    @Test
    void whenConfirmationNotFound_thenThrowTransactionException() {
        UUID transactionId = UUID.randomUUID();
        Transaction transaction = Mockito.mock(Transaction.class);
        Mockito.when(transaction.getId()).thenReturn(transactionId);
        TransactionConfirmationRequest request = Mockito.mock(TransactionConfirmationRequest.class);
        Mockito.when(request.getTransactionId()).thenReturn(transactionId);
        TransactionConfirmRepository transactionConfirmRepository = Mockito.mock(TransactionConfirmRepository.class);
        Mockito.when(transactionConfirmRepository.getById(transactionId)).thenReturn(transaction);
        ConfirmationConfirmRepository confirmRepository = Mockito.mock(ConfirmationConfirmRepository.class);
        Mockito.when(confirmRepository.findLastByTransactionId(transactionId)).thenReturn(null);
        TransactionStatusFactory statusFactory = new CoreTransactionStatusFactory();
        TransferserviceEventPublisher eventPublisher = Mockito.mock(TransferserviceEventPublisher.class);
        TransactionConfirmationInput sut = new CoreTransactionConfirmationInteractor(transactionConfirmRepository,
                confirmRepository, statusFactory, eventPublisher);

        TransactionException result = null;
        try {
            sut.confirm(request);
        } catch (TransactionException ex) {
            result = ex;
        }

        assertNotNull(result);
        assertEquals(TransactionExceptionCode.CONFIRMATION_NOT_FOUND.getId(), result.getId());
        assertEquals(TransactionExceptionCode.CONFIRMATION_NOT_FOUND.getMessage(), result.getMessage());
        Mockito.verify(transactionConfirmRepository, Mockito.times(1)).getById(Mockito.any(UUID.class));
        Mockito.verifyNoMoreInteractions(transactionConfirmRepository);
        Mockito.verify(confirmRepository, Mockito.times(1)).findLastByTransactionId(Mockito.any(UUID.class));
        Mockito.verifyNoMoreInteractions(confirmRepository);
        Mockito.verifyNoInteractions(eventPublisher);
    }

    @Test
    void whenConfirmationCodeNotValid_thenThrowTransactionException() {
        UUID transactionId = UUID.randomUUID();
        Transaction transaction = Mockito.mock(Transaction.class);
        Mockito.when(transaction.getId()).thenReturn(transactionId);
        TransactionConfirmationRequest request = Mockito.mock(TransactionConfirmationRequest.class);
        Mockito.when(request.getTransactionId()).thenReturn(transactionId);
        Mockito.when(request.getCode()).thenReturn("321");
        TransactionConfirmRepository transactionConfirmRepository = Mockito.mock(TransactionConfirmRepository.class);
        Mockito.when(transactionConfirmRepository.getById(transactionId)).thenReturn(transaction);
        ConfirmationConfirmRepository confirmRepository = Mockito.mock(ConfirmationConfirmRepository.class);
        Confirmation confirmation = new CoreConfirmation("123", transactionId);
        Mockito.when(confirmRepository.findLastByTransactionId(transactionId)).thenReturn(confirmation);
        TransactionStatusFactory statusFactory = new CoreTransactionStatusFactory();
        TransferserviceEventPublisher eventPublisher = Mockito.mock(TransferserviceEventPublisher.class);
        TransactionConfirmationInput sut = new CoreTransactionConfirmationInteractor(transactionConfirmRepository,
                confirmRepository, statusFactory, eventPublisher);

        TransactionException result = null;
        try {
            sut.confirm(request);
        } catch (TransactionException ex) {
            result = ex;
        }

        assertNotNull(result);
        assertEquals(TransactionExceptionCode.CONFIRMATION_CODE_INVALID.getId(), result.getId());
        assertEquals(TransactionExceptionCode.CONFIRMATION_CODE_INVALID.getMessage(), result.getMessage());
        Mockito.verify(transactionConfirmRepository, Mockito.times(1)).getById(Mockito.any(UUID.class));
        Mockito.verifyNoMoreInteractions(transactionConfirmRepository);
        Mockito.verify(confirmRepository, Mockito.times(1)).findLastByTransactionId(Mockito.any(UUID.class));
        Mockito.verify(confirmRepository, Mockito.times(1)).delete(confirmation);
        Mockito.verifyNoInteractions(eventPublisher);
    }

    @Test
    void whenConfirmationValidButTransactionNotUpdated_thenThrowTransactionException() {
        UUID transactionId = UUID.randomUUID();
        Transaction transaction = Mockito.mock(Transaction.class);
        Mockito.when(transaction.getId()).thenReturn(transactionId);
        TransactionConfirmationRequest request = Mockito.mock(TransactionConfirmationRequest.class);
        Mockito.when(request.getTransactionId()).thenReturn(transactionId);
        Mockito.when(request.getCode()).thenReturn("123");
        TransactionConfirmRepository transactionConfirmRepository = Mockito.mock(TransactionConfirmRepository.class);
        Mockito.when(transactionConfirmRepository.getById(transactionId)).thenReturn(transaction);
        Mockito.when(transactionConfirmRepository.update(Mockito.any(Transaction.class))).thenReturn(false);
        ConfirmationConfirmRepository confirmRepository = Mockito.mock(ConfirmationConfirmRepository.class);
        Confirmation confirmation = new CoreConfirmation("123", transactionId);
        Mockito.when(confirmRepository.findLastByTransactionId(transactionId)).thenReturn(confirmation);
        TransactionStatusFactory statusFactory = new CoreTransactionStatusFactory();
        TransferserviceEventPublisher eventPublisher = Mockito.mock(TransferserviceEventPublisher.class);
        TransactionConfirmationInput sut = new CoreTransactionConfirmationInteractor(transactionConfirmRepository,
                confirmRepository, statusFactory, eventPublisher);

        TransactionException result = null;
        try {
            sut.confirm(request);
        } catch (TransactionException ex) {
            result = ex;
        }

        assertNotNull(result);
        assertEquals(TransactionExceptionCode.CANNOT_UPDATE_TRANSACTION.getId(), result.getId());
        assertEquals(TransactionExceptionCode.CANNOT_UPDATE_TRANSACTION.getMessage(), result.getMessage());
        Mockito.verify(transactionConfirmRepository, Mockito.times(1)).getById(Mockito.any(UUID.class));
        Mockito.verify(transactionConfirmRepository, Mockito.times(1)).update(Mockito.any(Transaction.class));
        Mockito.verify(confirmRepository, Mockito.times(1)).findLastByTransactionId(Mockito.any(UUID.class));
        Mockito.verify(confirmRepository, Mockito.times(1)).delete(confirmation);
        Mockito.verifyNoInteractions(eventPublisher);
    }

    @Test
    void whenConfirmationValid_thenSuccess() {
        UUID transactionId = UUID.randomUUID();
        Transaction transaction = Mockito.mock(Transaction.class);
        Mockito.when(transaction.getId()).thenReturn(transactionId);
        TransactionConfirmationRequest request = Mockito.mock(TransactionConfirmationRequest.class);
        Mockito.when(request.getTransactionId()).thenReturn(transactionId);
        Mockito.when(request.getCode()).thenReturn("123");
        TransactionConfirmRepository transactionConfirmRepository = Mockito.mock(TransactionConfirmRepository.class);
        Mockito.when(transactionConfirmRepository.getById(transactionId)).thenReturn(transaction);
        Mockito.when(transactionConfirmRepository.update(Mockito.any(Transaction.class))).thenReturn(true);
        ConfirmationConfirmRepository confirmRepository = Mockito.mock(ConfirmationConfirmRepository.class);
        Confirmation confirmation = new CoreConfirmation("123", transactionId);
        Mockito.when(confirmRepository.findLastByTransactionId(transactionId)).thenReturn(confirmation);
        TransactionStatusFactory statusFactory = new CoreTransactionStatusFactory();
        TransferserviceEventPublisher eventPublisher = Mockito.mock(TransferserviceEventPublisher.class);
        TransactionConfirmationInput sut = new CoreTransactionConfirmationInteractor(transactionConfirmRepository,
                confirmRepository, statusFactory, eventPublisher);

        TransactionConfirmationResponse result = sut.confirm(request);

        assertNotNull(result);
        assertEquals(transactionId, result.getTransactionId());
        assertNotNull(result.getStatus());
        assertEquals(TransactionStatusCode.CONFIRMED, result.getStatus().getCode());
        Mockito.verify(transactionConfirmRepository, Mockito.times(1)).getById(Mockito.any(UUID.class));
        Mockito.verify(transactionConfirmRepository, Mockito.times(1)).update(Mockito.any(Transaction.class));
        Mockito.verify(confirmRepository, Mockito.times(1)).findLastByTransactionId(Mockito.any(UUID.class));
        Mockito.verify(confirmRepository, Mockito.times(1)).delete(confirmation);
        Mockito.verify(eventPublisher, Mockito.times(1)).publish(Mockito.any(TransactionIsConfirmed.class));
    }
}