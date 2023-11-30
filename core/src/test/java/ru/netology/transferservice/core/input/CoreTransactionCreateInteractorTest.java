package ru.netology.transferservice.core.input;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.netology.transferservice.contracts.entity.AccountAllocation;
import ru.netology.transferservice.contracts.entity.Card;
import ru.netology.transferservice.contracts.entity.Transaction;
import ru.netology.transferservice.contracts.entity.TransactionStatusCode;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CoreTransactionCreateInteractorTest {
    @Test
    void whenCardFromNotFound_thenNotFountException() {
        TransactionCreateRequest request = Mockito.mock(TransactionCreateRequest.class);
        Mockito.when(request.getCardFromNumber()).thenReturn("1234");
        Mockito.when(request.getCardToNumber()).thenReturn("4321");
        CardReadService cardReadService = Mockito.mock(CardReadService.class);
        Mockito.when(cardReadService.getCardByNumber("1234")).thenReturn(null);
        CommissionService commissionService = Mockito.mock(CommissionService.class);
        AccountAllocationService allocationService = Mockito.mock(AccountAllocationService.class);
        TransferserviceEventPublisher eventPublisher = Mockito.mock(TransferserviceEventPublisher.class);
        TransactionCreateRepository transactionCreateRepository = Mockito.mock(TransactionCreateRepository.class);
        TransactionStatusFactory statusFactory = new CoreTransactionStatusFactory();
        CardValidationService cardValidationService = Mockito.mock(CardValidationService.class);
        TransactionCreateInput sut = new CoreTransactionCreateInteractor(cardReadService, commissionService,
                allocationService, eventPublisher, transactionCreateRepository, statusFactory, cardValidationService);

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
        TransactionCreateRequest request = Mockito.mock(TransactionCreateRequest.class);
        Mockito.when(request.getCardFromNumber()).thenReturn("1234");
        Mockito.when(request.getCardToNumber()).thenReturn("4321");
        CardReadService cardReadService = Mockito.mock(CardReadService.class);
        Mockito.when(cardReadService.getCardByNumber("1234")).thenReturn(Mockito.mock(Card.class));
        Mockito.when(cardReadService.getCardByNumber("4321")).thenReturn(null);
        CommissionService commissionService = Mockito.mock(CommissionService.class);
        AccountAllocationService allocationService = Mockito.mock(AccountAllocationService.class);
        TransferserviceEventPublisher eventPublisher = Mockito.mock(TransferserviceEventPublisher.class);
        TransactionCreateRepository transactionCreateRepository = Mockito.mock(TransactionCreateRepository.class);
        TransactionStatusFactory statusFactory = new CoreTransactionStatusFactory();
        CardValidationService cardValidationService = Mockito.mock(CardValidationService.class);
        TransactionCreateInput sut = new CoreTransactionCreateInteractor(cardReadService, commissionService,
                allocationService, eventPublisher, transactionCreateRepository, statusFactory, cardValidationService);

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
        TransactionCreateRequest request = Mockito.mock(TransactionCreateRequest.class);
        Mockito.when(request.getCardFromNumber()).thenReturn("1234");
        Mockito.when(request.getCardToNumber()).thenReturn("4321");
        CardReadService cardReadService = Mockito.mock(CardReadService.class);
        Mockito.when(cardReadService.getCardByNumber("1234")).thenReturn(Mockito.mock(Card.class));
        Mockito.when(cardReadService.getCardByNumber("4321")).thenReturn(Mockito.mock(Card.class));
        CommissionService commissionService = Mockito.mock(CommissionService.class);
        AccountAllocationService allocationService = Mockito.mock(AccountAllocationService.class);
        TransferserviceEventPublisher eventPublisher = Mockito.mock(TransferserviceEventPublisher.class);
        TransactionCreateRepository transactionCreateRepository = Mockito.mock(TransactionCreateRepository.class);
        TransactionStatusFactory statusFactory = new CoreTransactionStatusFactory();
        CardValidationService cardValidationService = Mockito.mock(CardValidationService.class);
        Mockito.when(cardValidationService.isValid(Mockito.any(), Mockito.any())).thenReturn(false);
        TransactionCreateInput sut = new CoreTransactionCreateInteractor(cardReadService, commissionService,
                allocationService, eventPublisher, transactionCreateRepository, statusFactory, cardValidationService);

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
        TransactionCreateRequest request = Mockito.mock(TransactionCreateRequest.class);
        Mockito.when(request.getAmount()).thenReturn(1000L);
        Mockito.when(request.getCurrency()).thenReturn("RUB");
        Mockito.when(request.getCardFromNumber()).thenReturn("1234");
        Mockito.when(request.getCardToNumber()).thenReturn("4321");
        CardReadService cardReadService = Mockito.mock(CardReadService.class);
        Mockito.when(cardReadService.getCardByNumber("1234")).thenReturn(getTestCard());
        Mockito.when(cardReadService.getCardByNumber("4321")).thenReturn(Mockito.mock(Card.class));
        CommissionService commissionService = Mockito.mock(CommissionService.class);
        Mockito.when(commissionService.compute(Mockito.any(Card.class), Mockito.any(Card.class), Mockito.anyLong(), Mockito.anyString())).thenReturn(new CoreCommission(10));
        AccountAllocationService allocationService = Mockito.mock(AccountAllocationService.class);
        Mockito.when(allocationService.allocate(Mockito.any(), Mockito.anyString(), Mockito.anyLong())).thenReturn(null);
        TransferserviceEventPublisher eventPublisher = Mockito.mock(TransferserviceEventPublisher.class);
        TransactionCreateRepository transactionCreateRepository = Mockito.mock(TransactionCreateRepository.class);
        TransactionStatusFactory statusFactory = new CoreTransactionStatusFactory();
        CardValidationService cardValidationService = Mockito.mock(CardValidationService.class);
        Mockito.when(cardValidationService.isValid(Mockito.any(), Mockito.any())).thenReturn(true);
        TransactionCreateInput sut = new CoreTransactionCreateInteractor(cardReadService, commissionService,
                allocationService, eventPublisher, transactionCreateRepository, statusFactory, cardValidationService);

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
        TransactionCreateRequest request = Mockito.mock(TransactionCreateRequest.class);
        Mockito.when(request.getAmount()).thenReturn(1000L);
        Mockito.when(request.getCurrency()).thenReturn("RUB");
        Mockito.when(request.getCardFromNumber()).thenReturn("1234");
        Mockito.when(request.getCardToNumber()).thenReturn("4321");
        CardReadService cardReadService = Mockito.mock(CardReadService.class);
        Mockito.when(cardReadService.getCardByNumber("1234")).thenReturn(getTestCard());
        Mockito.when(cardReadService.getCardByNumber("4321")).thenReturn(Mockito.mock(Card.class));
        CommissionService commissionService = Mockito.mock(CommissionService.class);
        Mockito.when(commissionService.compute(Mockito.any(Card.class), Mockito.any(Card.class), Mockito.anyLong(), Mockito.anyString())).thenReturn(new CoreCommission(10));
        AccountAllocationService allocationService = Mockito.mock(AccountAllocationService.class);
        Mockito.when(allocationService.allocate(Mockito.any(), Mockito.anyString(), Mockito.anyLong())).thenReturn(Mockito.mock(AccountAllocation.class));
        TransferserviceEventPublisher eventPublisher = Mockito.mock(TransferserviceEventPublisher.class);
        TransactionCreateRepository transactionCreateRepository = Mockito.mock(TransactionCreateRepository.class);
        Mockito.when(transactionCreateRepository.store(Mockito.any(Transaction.class))).thenReturn(false);
        TransactionStatusFactory statusFactory = new CoreTransactionStatusFactory();
        CardValidationService cardValidationService = Mockito.mock(CardValidationService.class);
        Mockito.when(cardValidationService.isValid(Mockito.any(), Mockito.any())).thenReturn(true);
        TransactionCreateInput sut = new CoreTransactionCreateInteractor(cardReadService, commissionService,
                allocationService, eventPublisher, transactionCreateRepository, statusFactory, cardValidationService);

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
        TransactionCreateRequest request = Mockito.mock(TransactionCreateRequest.class);
        Mockito.when(request.getAmount()).thenReturn(1000L);
        Mockito.when(request.getCurrency()).thenReturn("RUB");
        Mockito.when(request.getCardFromNumber()).thenReturn("1234");
        Mockito.when(request.getCardToNumber()).thenReturn("4321");
        CardReadService cardReadService = Mockito.mock(CardReadService.class);
        Mockito.when(cardReadService.getCardByNumber("1234")).thenReturn(getTestCard());
        Mockito.when(cardReadService.getCardByNumber("4321")).thenReturn(Mockito.mock(Card.class));
        CommissionService commissionService = Mockito.mock(CommissionService.class);
        Mockito.when(commissionService.compute(Mockito.any(Card.class), Mockito.any(Card.class), Mockito.anyLong(), Mockito.anyString())).thenReturn(new CoreCommission(10));
        AccountAllocationService allocationService = Mockito.mock(AccountAllocationService.class);
        Mockito.when(allocationService.allocate(Mockito.any(), Mockito.anyString(), Mockito.anyLong())).thenReturn(Mockito.mock(AccountAllocation.class));
        TransferserviceEventPublisher eventPublisher = Mockito.mock(TransferserviceEventPublisher.class);
        TransactionCreateRepository transactionCreateRepository = Mockito.mock(TransactionCreateRepository.class);
        Mockito.when(transactionCreateRepository.store(Mockito.any(Transaction.class))).thenReturn(true);
        TransactionStatusFactory statusFactory = new CoreTransactionStatusFactory();
        CardValidationService cardValidationService = Mockito.mock(CardValidationService.class);
        Mockito.when(cardValidationService.isValid(Mockito.any(), Mockito.any())).thenReturn(true);
        TransactionCreateInput sut = new CoreTransactionCreateInteractor(cardReadService, commissionService,
                allocationService, eventPublisher, transactionCreateRepository, statusFactory, cardValidationService);

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
        Mockito.verify(eventPublisher, Mockito.times(1)).publish(Mockito.any(TransactionIsCreated.class));
        Mockito.verify(transactionCreateRepository, Mockito.times(1)).store(Mockito.any());
        Mockito.verify(cardValidationService, Mockito.times(1)).isValid(Mockito.any(), Mockito.any());
    }

    private Card getTestCard() {
        return new CoreCard("1234", "09/55", "123", "1234", false);
    }
}
