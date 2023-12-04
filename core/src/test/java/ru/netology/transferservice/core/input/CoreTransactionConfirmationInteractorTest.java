package ru.netology.transferservice.core.input;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.netology.transferservice.contracts.entity.Confirmation;
import ru.netology.transferservice.contracts.entity.Transaction;
import ru.netology.transferservice.contracts.entity.TransactionStatusCode;
import ru.netology.transferservice.contracts.event.TransactionEvent;
import ru.netology.transferservice.contracts.event.TransactionIsConfirmed;
import ru.netology.transferservice.contracts.event.TransferserviceEventPublisher;
import ru.netology.transferservice.contracts.exception.TransactionException;
import ru.netology.transferservice.contracts.exception.TransactionExceptionCode;
import ru.netology.transferservice.contracts.factory.TransactionStatusFactory;
import ru.netology.transferservice.contracts.input.TransactionConfirmationRequest;
import ru.netology.transferservice.contracts.input.TransactionConfirmationResponse;
import ru.netology.transferservice.contracts.repository.ConfirmationConfirmRepository;
import ru.netology.transferservice.contracts.repository.TransactionConfirmRepository;
import ru.netology.transferservice.core.entity.CoreConfirmation;
import ru.netology.transferservice.core.factory.CoreTransactionStatusFactory;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CoreTransactionConfirmationInteractorTest {

    private final TransactionStatusFactory statusFactory = new CoreTransactionStatusFactory();
    @Mock
    private TransactionConfirmationRequest request;
    @Mock
    private Transaction transaction;
    @Mock
    private TransactionConfirmRepository transactionConfirmRepository;
    @Mock
    private ConfirmationConfirmRepository confirmRepository;
    @Mock
    private TransferserviceEventPublisher eventPublisher;

    private CoreTransactionConfirmationInteractor sut;

    @BeforeEach
    void setUp() {
        sut = new CoreTransactionConfirmationInteractor(transactionConfirmRepository, confirmRepository, statusFactory,
                eventPublisher);
    }

    @Test
    void whenTransactionNotFound_thenThrowTransactionException() {
        UUID transactionId = UUID.randomUUID();
        Mockito.when(request.getTransactionId()).thenReturn(transactionId);
        Mockito.when(transactionConfirmRepository.getById(transactionId)).thenReturn(null);

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
        Mockito.when(request.getTransactionId()).thenReturn(transactionId);
        Mockito.when(transactionConfirmRepository.getById(transactionId)).thenReturn(transaction);
        Mockito.when(confirmRepository.findLastByTransactionId(transactionId)).thenReturn(null);

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
        Mockito.when(request.getTransactionId()).thenReturn(transactionId);
        Mockito.when(request.getCode()).thenReturn("321");
        Mockito.when(transactionConfirmRepository.getById(transactionId)).thenReturn(transaction);
        Confirmation confirmation = new CoreConfirmation("123", transactionId);
        Mockito.when(confirmRepository.findLastByTransactionId(transactionId)).thenReturn(confirmation);

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
        Mockito.when(transaction.getId()).thenReturn(transactionId);
        Mockito.when(request.getTransactionId()).thenReturn(transactionId);
        Mockito.when(request.getCode()).thenReturn("123");
        Mockito.when(transactionConfirmRepository.getById(transactionId)).thenReturn(transaction);
        Mockito.when(transactionConfirmRepository.update(Mockito.any(Transaction.class))).thenReturn(false);
        Confirmation confirmation = new CoreConfirmation("123", transactionId);
        Mockito.when(confirmRepository.findLastByTransactionId(transactionId)).thenReturn(confirmation);

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
        Mockito.when(transaction.getId()).thenReturn(transactionId);
        Mockito.when(request.getTransactionId()).thenReturn(transactionId);
        Mockito.when(request.getCode()).thenReturn("123");
        Mockito.when(transactionConfirmRepository.getById(transactionId)).thenReturn(transaction);
        Mockito.when(transactionConfirmRepository.update(Mockito.any(Transaction.class))).thenReturn(true);
        Confirmation confirmation = new CoreConfirmation("123", transactionId);
        Mockito.when(confirmRepository.findLastByTransactionId(transactionId)).thenReturn(confirmation);
        ArgumentCaptor<TransactionEvent> eventCaptor = ArgumentCaptor.forClass(TransactionEvent.class);

        TransactionConfirmationResponse result = sut.confirm(request);

        assertNotNull(result);
        assertEquals(transactionId, result.getTransactionId());
        assertNotNull(result.getStatus());
        assertEquals(TransactionStatusCode.CONFIRMED, result.getStatus().getCode());
        Mockito.verify(transactionConfirmRepository, Mockito.times(1)).getById(Mockito.any(UUID.class));
        Mockito.verify(transactionConfirmRepository, Mockito.times(1)).update(Mockito.any(Transaction.class));
        Mockito.verify(confirmRepository, Mockito.times(1)).findLastByTransactionId(Mockito.any(UUID.class));
        Mockito.verify(confirmRepository, Mockito.times(1)).delete(confirmation);
        Mockito.verify(eventPublisher, Mockito.times(1)).publish(eventCaptor.capture());
        TransactionEvent capturedEvent = eventCaptor.getValue();
        assertNotNull(capturedEvent);
        assertInstanceOf(TransactionIsConfirmed.class, capturedEvent);
        assertNotNull(capturedEvent.getTransaction());
        assertEquals(result.getTransactionId(), capturedEvent.getTransaction().getId());
        assertEquals(result.getStatus(), capturedEvent.getTransaction().getStatus());
    }
}