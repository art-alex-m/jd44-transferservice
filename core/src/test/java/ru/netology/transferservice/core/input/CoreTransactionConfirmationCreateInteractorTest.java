package ru.netology.transferservice.core.input;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.netology.transferservice.contracts.entity.Confirmation;
import ru.netology.transferservice.contracts.event.ConfirmationIsCreated;
import ru.netology.transferservice.contracts.event.TransferserviceEventPublisher;
import ru.netology.transferservice.contracts.exception.TransactionException;
import ru.netology.transferservice.contracts.exception.TransactionExceptionCode;
import ru.netology.transferservice.contracts.factory.ConfirmationFactory;
import ru.netology.transferservice.contracts.input.TransactionConfirmationCreateInput;
import ru.netology.transferservice.contracts.input.TransactionConfirmationCreateRequest;
import ru.netology.transferservice.contracts.input.TransactionConfirmationCreateResponse;
import ru.netology.transferservice.contracts.repository.ConfirmationCreateRepository;
import ru.netology.transferservice.core.entity.CoreConfirmation;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class CoreTransactionConfirmationCreateInteractorTest {

    @Mock
    private TransactionConfirmationCreateRequest request;

    @Mock
    private ConfirmationFactory confirmationFactory;

    @Mock
    private ConfirmationCreateRepository confirmationCreateRepository;

    @Mock
    private TransferserviceEventPublisher eventPublisher;

    private TransactionConfirmationCreateInput sut;

    @BeforeEach
    void setUp() {
        sut = new CoreTransactionConfirmationCreateInteractor(confirmationFactory, confirmationCreateRepository,
                eventPublisher);
    }

    @Test
    void whenConfirmationNull_thenThrowTransactionException() {

        TransactionException result = null;
        try {
            sut.create(request);
        } catch (TransactionException ex) {
            result = ex;
        }

        assertNotNull(result);
        assertEquals(TransactionExceptionCode.CANNOT_CREATE_CONFIRMATION.getId(), result.getId());
        assertEquals(TransactionExceptionCode.CANNOT_CREATE_CONFIRMATION.getMessage(), result.getMessage());
        Mockito.verify(confirmationFactory, Mockito.times(1)).create(Mockito.any());
        Mockito.verifyNoInteractions(confirmationCreateRepository);
        Mockito.verifyNoInteractions(eventPublisher);
    }

    @Test
    void whenConfirmationDidNotStore_thenThrowTransactionException() {
        UUID transactionId = UUID.randomUUID();
        Confirmation confirmation = new CoreConfirmation("1234", transactionId);
        Mockito.when(request.getTransactionId()).thenReturn(transactionId);
        Mockito.when(confirmationFactory.create(transactionId)).thenReturn(confirmation);
        Mockito.when(confirmationCreateRepository.store(Mockito.any(Confirmation.class))).thenReturn(false);

        TransactionException result = null;
        try {
            sut.create(request);
        } catch (TransactionException ex) {
            result = ex;
        }

        assertNotNull(result);
        assertEquals(TransactionExceptionCode.CANNOT_CREATE_CONFIRMATION.getId(), result.getId());
        assertEquals(TransactionExceptionCode.CANNOT_CREATE_CONFIRMATION.getMessage(), result.getMessage());
        Mockito.verify(confirmationFactory, Mockito.times(1)).create(transactionId);
        Mockito.verify(confirmationCreateRepository, Mockito.times(1)).store(confirmation);
        Mockito.verifyNoInteractions(eventPublisher);
    }

    @Test
    void whenAllSuccess_thenSuccess() {
        UUID transactionId = UUID.randomUUID();
        Confirmation confirmation = new CoreConfirmation("1234", transactionId);
        Mockito.when(request.getTransactionId()).thenReturn(transactionId);
        Mockito.when(confirmationFactory.create(transactionId)).thenReturn(confirmation);
        Mockito.when(confirmationCreateRepository.store(Mockito.any(Confirmation.class))).thenReturn(true);
        ArgumentCaptor<ConfirmationIsCreated> eventCaptor = ArgumentCaptor.forClass(ConfirmationIsCreated.class);

        TransactionConfirmationCreateResponse result = sut.create(request);

        assertEquals(transactionId, result.getTransactionId());
        assertEquals(confirmation.getCode(), result.getCode());
        Mockito.verify(confirmationFactory, Mockito.times(1)).create(transactionId);
        Mockito.verify(confirmationCreateRepository, Mockito.times(1)).store(confirmation);
        Mockito.verify(eventPublisher, Mockito.times(1)).publish(eventCaptor.capture());
        ConfirmationIsCreated capturedEvent = eventCaptor.getValue();
        assertNotNull(capturedEvent);
        assertNotNull(capturedEvent.getConfirmation());
        assertEquals(confirmation, capturedEvent.getConfirmation());
    }
}
