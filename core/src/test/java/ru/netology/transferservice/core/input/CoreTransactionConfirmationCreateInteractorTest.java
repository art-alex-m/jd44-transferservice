package ru.netology.transferservice.core.input;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.netology.transferservice.contracts.entity.Confirmation;
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

class CoreTransactionConfirmationCreateInteractorTest {

    @Test
    void whenConfirmationNull_thenThrowTransactionException() {
        TransactionConfirmationCreateRequest request = Mockito.mock(TransactionConfirmationCreateRequest.class);
        ConfirmationFactory confirmationFactory = Mockito.mock(ConfirmationFactory.class);
        ConfirmationCreateRepository confirmationCreateRepository = Mockito.mock(ConfirmationCreateRepository.class);
        TransactionConfirmationCreateInput sut = new CoreTransactionConfirmationCreateInteractor(confirmationFactory,
                confirmationCreateRepository);

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
        Mockito.verify(confirmationCreateRepository, Mockito.never()).store(Mockito.any(Confirmation.class));
    }

    @Test
    void whenConfirmationDoesNotStore_thenThrowTransactionException() {
        UUID transactionId = UUID.randomUUID();
        Confirmation confirmation = new CoreConfirmation("1234", transactionId);
        TransactionConfirmationCreateRequest request = Mockito.mock(TransactionConfirmationCreateRequest.class);
        Mockito.when(request.getTransactionId()).thenReturn(transactionId);
        ConfirmationFactory confirmationFactory = Mockito.mock(ConfirmationFactory.class);
        Mockito.when(confirmationFactory.create(transactionId)).thenReturn(confirmation);
        ConfirmationCreateRepository confirmationCreateRepository = Mockito.mock(ConfirmationCreateRepository.class);
        Mockito.when(confirmationCreateRepository.store(Mockito.any(Confirmation.class))).thenReturn(false);
        TransactionConfirmationCreateInput sut = new CoreTransactionConfirmationCreateInteractor(confirmationFactory,
                confirmationCreateRepository);

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
    }

    @Test
    void whenAllSuccess_thenSuccess() {
        UUID transactionId = UUID.randomUUID();
        Confirmation confirmation = new CoreConfirmation("1234", transactionId);
        TransactionConfirmationCreateRequest request = Mockito.mock(TransactionConfirmationCreateRequest.class);
        Mockito.when(request.getTransactionId()).thenReturn(transactionId);
        ConfirmationFactory confirmationFactory = Mockito.mock(ConfirmationFactory.class);
        Mockito.when(confirmationFactory.create(transactionId)).thenReturn(confirmation);
        ConfirmationCreateRepository confirmationCreateRepository = Mockito.mock(ConfirmationCreateRepository.class);
        Mockito.when(confirmationCreateRepository.store(Mockito.any(Confirmation.class))).thenReturn(true);
        TransactionConfirmationCreateInput sut = new CoreTransactionConfirmationCreateInteractor(confirmationFactory,
                confirmationCreateRepository);

        TransactionConfirmationCreateResponse result = sut.create(request);

        assertEquals(transactionId, result.getTransactionId());
        assertEquals(confirmation.getCode(), result.getCode());
        Mockito.verify(confirmationFactory, Mockito.times(1)).create(transactionId);
        Mockito.verify(confirmationCreateRepository, Mockito.times(1)).store(confirmation);
    }
}
