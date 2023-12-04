package ru.netology.transferservice.core.input;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.netology.transferservice.contracts.entity.AccountAllocation;
import ru.netology.transferservice.contracts.entity.Card;
import ru.netology.transferservice.contracts.entity.Transaction;
import ru.netology.transferservice.contracts.entity.TransactionStatusCode;
import ru.netology.transferservice.contracts.event.TransactionEvent;
import ru.netology.transferservice.contracts.event.TransactionIsCreated;
import ru.netology.transferservice.contracts.event.TransferserviceEventPublisher;
import ru.netology.transferservice.contracts.exception.TransactionException;
import ru.netology.transferservice.contracts.exception.TransactionExceptionCode;
import ru.netology.transferservice.contracts.factory.TransactionStatusFactory;
import ru.netology.transferservice.contracts.input.TransactionCreateInput;
import ru.netology.transferservice.contracts.input.TransactionCreateRequest;
import ru.netology.transferservice.contracts.input.TransactionCreateResponse;
import ru.netology.transferservice.contracts.repository.TransactionCreateRepository;
import ru.netology.transferservice.contracts.service.AccountAllocationService;
import ru.netology.transferservice.contracts.service.CardReadService;
import ru.netology.transferservice.contracts.service.CardValidationService;
import ru.netology.transferservice.contracts.service.CommissionService;
import ru.netology.transferservice.core.entity.CoreCard;
import ru.netology.transferservice.core.entity.CoreCommission;
import ru.netology.transferservice.core.factory.CoreTransactionStatusFactory;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CoreTransactionCreateInteractorTest {

    private final TransactionStatusFactory statusFactory = new CoreTransactionStatusFactory();
    @Mock
    private TransactionCreateRequest request;
    @Mock
    private CardReadService cardReadService;
    @Mock
    private CommissionService commissionService;
    @Mock
    private AccountAllocationService allocationService;
    @Mock
    private TransferserviceEventPublisher eventPublisher;
    @Mock
    private TransactionCreateRepository transactionCreateRepository;
    @Mock
    private CardValidationService cardValidationService;
    private TransactionCreateInput sut;

    @BeforeEach
    void setUp() {
        sut = new CoreTransactionCreateInteractor(cardReadService, commissionService, allocationService, eventPublisher,
                transactionCreateRepository, statusFactory, cardValidationService);
    }

    @Test
    void whenCardFromNotFound_thenNotFountException() {
        Mockito.when(request.getCardFromNumber()).thenReturn("1234");
        Mockito.when(cardReadService.getCardByNumber("1234")).thenReturn(null);

        TransactionException result = null;
        try {
            sut.create(request);
        } catch (TransactionException ex) {
            result = ex;
        }

        assertNotNull(result);
        assertEquals(TransactionExceptionCode.CARD_FROM_NOT_FOUND.getId(), result.getId());
        assertEquals(TransactionExceptionCode.CARD_FROM_NOT_FOUND.getMessage(), result.getMessage());
        Mockito.verify(request, Mockito.never()).getCardToNumber();
        Mockito.verify(cardReadService, Mockito.times(1)).getCardByNumber("1234");
        Mockito.verify(cardReadService, Mockito.never()).getCardByNumber("4321");
        Mockito.verify(commissionService, Mockito.never()).compute(Mockito.any(), Mockito.any(), Mockito.anyLong(), Mockito.anyString());
        Mockito.verify(allocationService, Mockito.never()).allocate(Mockito.any(), Mockito.anyString(), Mockito.anyLong());
        Mockito.verify(eventPublisher, Mockito.never()).publish(Mockito.any());
        Mockito.verify(transactionCreateRepository, Mockito.never()).store(Mockito.any());
        Mockito.verify(cardValidationService, Mockito.never()).isValid(Mockito.any(), Mockito.any());
    }

    @Test
    void whenCardToNotFound_thenNotFountException() {
        Mockito.when(request.getCardFromNumber()).thenReturn("1234");
        Mockito.when(request.getCardToNumber()).thenReturn("4321");
        Mockito.when(cardReadService.getCardByNumber("1234")).thenReturn(Mockito.mock(Card.class));
        Mockito.when(cardReadService.getCardByNumber("4321")).thenReturn(null);

        TransactionException result = null;
        try {
            sut.create(request);
        } catch (TransactionException ex) {
            result = ex;
        }

        assertNotNull(result);
        assertEquals(TransactionExceptionCode.CARD_TO_NOT_FOUND.getId(), result.getId());
        assertEquals(TransactionExceptionCode.CARD_TO_NOT_FOUND.getMessage(), result.getMessage());
        Mockito.verify(cardReadService, Mockito.times(1)).getCardByNumber("1234");
        Mockito.verify(cardReadService, Mockito.times(1)).getCardByNumber("4321");
        Mockito.verify(commissionService, Mockito.never()).compute(Mockito.any(), Mockito.any(), Mockito.anyLong(), Mockito.anyString());
        Mockito.verify(allocationService, Mockito.never()).allocate(Mockito.any(), Mockito.anyString(), Mockito.anyLong());
        Mockito.verify(eventPublisher, Mockito.never()).publish(Mockito.any());
        Mockito.verify(transactionCreateRepository, Mockito.never()).store(Mockito.any());
        Mockito.verify(cardValidationService, Mockito.never()).isValid(Mockito.any(), Mockito.any());
    }

    @Test
    void whenCardFromNotValid_thenNotValidException() {
        Mockito.when(request.getCardFromNumber()).thenReturn("1234");
        Mockito.when(request.getCardToNumber()).thenReturn("4321");
        Mockito.when(cardReadService.getCardByNumber("1234")).thenReturn(Mockito.mock(Card.class));
        Mockito.when(cardReadService.getCardByNumber("4321")).thenReturn(Mockito.mock(Card.class));
        Mockito.when(cardValidationService.isValid(Mockito.any(), Mockito.any())).thenReturn(false);

        TransactionException result = null;
        try {
            sut.create(request);
        } catch (TransactionException ex) {
            result = ex;
        }

        assertNotNull(result);
        assertEquals(TransactionExceptionCode.CARD_FROM_NOT_VALID.getId(), result.getId());
        assertEquals(TransactionExceptionCode.CARD_FROM_NOT_VALID.getMessage(), result.getMessage());
        Mockito.verify(cardReadService, Mockito.times(1)).getCardByNumber("1234");
        Mockito.verify(cardReadService, Mockito.times(1)).getCardByNumber("4321");
        Mockito.verify(commissionService, Mockito.never()).compute(Mockito.any(), Mockito.any(), Mockito.anyLong(), Mockito.anyString());
        Mockito.verify(allocationService, Mockito.never()).allocate(Mockito.any(), Mockito.anyString(), Mockito.anyLong());
        Mockito.verify(allocationService, Mockito.never()).delete(Mockito.any());
        Mockito.verify(eventPublisher, Mockito.never()).publish(Mockito.any());
        Mockito.verify(transactionCreateRepository, Mockito.never()).store(Mockito.any());
        Mockito.verify(cardValidationService, Mockito.times(1)).isValid(Mockito.any(), Mockito.any());
    }

    @Test
    void whenAllocationFail_thenCannotAllocateException() {
        Mockito.when(request.getAmount()).thenReturn(1000L);
        Mockito.when(request.getCurrency()).thenReturn("RUB");
        Mockito.when(request.getCardFromNumber()).thenReturn("1234");
        Mockito.when(request.getCardToNumber()).thenReturn("4321");
        Mockito.when(cardReadService.getCardByNumber("1234")).thenReturn(getTestCard());
        Mockito.when(cardReadService.getCardByNumber("4321")).thenReturn(Mockito.mock(Card.class));
        Mockito.when(commissionService.compute(Mockito.any(Card.class), Mockito.any(Card.class), Mockito.anyLong(), Mockito.anyString())).thenReturn(new CoreCommission(10));
        Mockito.when(allocationService.allocate(Mockito.any(), Mockito.anyString(), Mockito.anyLong())).thenReturn(null);
        Mockito.when(cardValidationService.isValid(Mockito.any(), Mockito.any())).thenReturn(true);

        TransactionException result = null;
        try {
            sut.create(request);
        } catch (TransactionException ex) {
            result = ex;
        }

        assertNotNull(result);
        assertEquals(TransactionExceptionCode.CANNOT_ALLOCATE_SUM.getId(), result.getId());
        assertEquals(TransactionExceptionCode.CANNOT_ALLOCATE_SUM.getMessage(), result.getMessage());
        Mockito.verify(cardReadService, Mockito.times(1)).getCardByNumber("1234");
        Mockito.verify(cardReadService, Mockito.times(1)).getCardByNumber("4321");
        Mockito.verify(commissionService, Mockito.times(1)).compute(Mockito.any(), Mockito.any(), Mockito.anyLong(), Mockito.anyString());
        Mockito.verify(allocationService, Mockito.times(1)).allocate(Mockito.any(), Mockito.anyString(), Mockito.anyLong());
        Mockito.verify(eventPublisher, Mockito.never()).publish(Mockito.any());
        Mockito.verify(transactionCreateRepository, Mockito.never()).store(Mockito.any());
        Mockito.verify(cardValidationService, Mockito.times(1)).isValid(Mockito.any(), Mockito.any());
    }

    @Test
    void whenTransactionStoreFail_thenCannotCreateTransactionException() {
        Mockito.when(request.getAmount()).thenReturn(1000L);
        Mockito.when(request.getCurrency()).thenReturn("RUB");
        Mockito.when(request.getCardFromNumber()).thenReturn("1234");
        Mockito.when(request.getCardToNumber()).thenReturn("4321");
        Mockito.when(cardReadService.getCardByNumber("1234")).thenReturn(getTestCard());
        Mockito.when(cardReadService.getCardByNumber("4321")).thenReturn(Mockito.mock(Card.class));
        Mockito.when(commissionService.compute(Mockito.any(Card.class), Mockito.any(Card.class), Mockito.anyLong(), Mockito.anyString())).thenReturn(new CoreCommission(10));
        Mockito.when(allocationService.allocate(Mockito.any(), Mockito.anyString(), Mockito.anyLong())).thenReturn(Mockito.mock(AccountAllocation.class));
        Mockito.when(transactionCreateRepository.store(Mockito.any(Transaction.class))).thenReturn(false);
        Mockito.when(cardValidationService.isValid(Mockito.any(), Mockito.any())).thenReturn(true);

        TransactionException result = null;
        try {
            sut.create(request);
        } catch (TransactionException ex) {
            result = ex;
        }

        assertNotNull(result);
        assertEquals(TransactionExceptionCode.CANNOT_CREATE_TRANSACTION.getId(), result.getId());
        assertEquals(TransactionExceptionCode.CANNOT_CREATE_TRANSACTION.getMessage(), result.getMessage());
        Mockito.verify(cardReadService, Mockito.times(1)).getCardByNumber("1234");
        Mockito.verify(cardReadService, Mockito.times(1)).getCardByNumber("4321");
        Mockito.verify(commissionService, Mockito.times(1)).compute(Mockito.any(), Mockito.any(), Mockito.anyLong(), Mockito.anyString());
        Mockito.verify(allocationService, Mockito.times(1)).allocate(Mockito.any(), Mockito.anyString(), Mockito.anyLong());
        Mockito.verify(allocationService, Mockito.times(1)).delete(Mockito.any());
        Mockito.verify(eventPublisher, Mockito.never()).publish(Mockito.any());
        Mockito.verify(transactionCreateRepository, Mockito.times(1)).store(Mockito.any());
        Mockito.verify(cardValidationService, Mockito.times(1)).isValid(Mockito.any(), Mockito.any());
    }

    @Test
    void whenSuccessRequest_thenSuccess() {
        Mockito.when(request.getAmount()).thenReturn(1000L);
        Mockito.when(request.getCurrency()).thenReturn("RUB");
        Mockito.when(request.getCardFromNumber()).thenReturn("1234");
        Mockito.when(request.getCardToNumber()).thenReturn("4321");
        Mockito.when(cardReadService.getCardByNumber("1234")).thenReturn(getTestCard());
        Mockito.when(cardReadService.getCardByNumber("4321")).thenReturn(Mockito.mock(Card.class));
        Mockito.when(commissionService.compute(Mockito.any(Card.class), Mockito.any(Card.class), Mockito.anyLong(), Mockito.anyString())).thenReturn(new CoreCommission(10));
        Mockito.when(allocationService.allocate(Mockito.any(), Mockito.anyString(), Mockito.anyLong())).thenReturn(Mockito.mock(AccountAllocation.class));
        Mockito.when(transactionCreateRepository.store(Mockito.any(Transaction.class))).thenReturn(true);
        Mockito.when(cardValidationService.isValid(Mockito.any(), Mockito.any())).thenReturn(true);
        ArgumentCaptor<TransactionEvent> eventCaptor = ArgumentCaptor.forClass(TransactionEvent.class);

        TransactionCreateResponse result = sut.create(request);

        assertNotNull(result);
        assertNotNull(result.getTransactionId());
        assertNotNull(result.getStatus());
        assertEquals(TransactionStatusCode.NEW, result.getStatus().getCode());
        Mockito.verify(cardReadService, Mockito.times(1)).getCardByNumber("1234");
        Mockito.verify(cardReadService, Mockito.times(1)).getCardByNumber("4321");
        Mockito.verify(commissionService, Mockito.times(1)).compute(Mockito.any(), Mockito.any(), Mockito.anyLong(), Mockito.anyString());
        Mockito.verify(allocationService, Mockito.times(1)).allocate(Mockito.any(), Mockito.anyString(), Mockito.anyLong());
        Mockito.verify(allocationService, Mockito.never()).delete(Mockito.any());
        Mockito.verify(transactionCreateRepository, Mockito.times(1)).store(Mockito.any());
        Mockito.verify(cardValidationService, Mockito.times(1)).isValid(Mockito.any(), Mockito.any());
        Mockito.verify(eventPublisher, Mockito.times(1)).publish(eventCaptor.capture());
        TransactionEvent capturedEvent = eventCaptor.getValue();
        assertNotNull(capturedEvent);
        assertInstanceOf(TransactionIsCreated.class, capturedEvent);
        assertNotNull(capturedEvent.getTransaction());
        assertEquals(result.getTransactionId(), capturedEvent.getTransaction().getId());
        assertEquals(result.getStatus(), capturedEvent.getTransaction().getStatus());
    }

    private Card getTestCard() {
        return new CoreCard("1234", "09/55", "123", "1234", false);
    }
}
