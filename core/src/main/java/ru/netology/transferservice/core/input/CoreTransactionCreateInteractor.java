package ru.netology.transferservice.core.input;

import ru.netology.transferservice.contracts.entity.AccountAllocation;
import ru.netology.transferservice.contracts.entity.Card;
import ru.netology.transferservice.contracts.entity.Transaction;
import ru.netology.transferservice.contracts.entity.TransactionStatusCode;
import ru.netology.transferservice.contracts.event.TransactionIsCreated;
import ru.netology.transferservice.contracts.event.TransferserviceEventPublisher;
import ru.netology.transferservice.contracts.exception.TransactionException;
import ru.netology.transferservice.contracts.exception.TransactionExceptionCode;
import ru.netology.transferservice.contracts.factory.TransactionBuilder;
import ru.netology.transferservice.contracts.factory.TransactionStatusFactory;
import ru.netology.transferservice.contracts.input.TransactionCreateInput;
import ru.netology.transferservice.contracts.input.TransactionCreateRequest;
import ru.netology.transferservice.contracts.input.TransactionCreateResponse;
import ru.netology.transferservice.contracts.repository.TransactionCreateRepository;
import ru.netology.transferservice.contracts.service.*;
import ru.netology.transferservice.core.factory.CoreTransactionBuilder;
import ru.netology.transferservice.core.service.CoreCardUserInput;

public class CoreTransactionCreateInteractor implements TransactionCreateInput {
    private final CardReadService cardReadService;
    private final CommissionService commissionService;
    private final AccountAllocationService allocationService;
    private final TransferserviceEventPublisher eventPublisher;
    private final TransactionCreateRepository transactionCreateRepository;
    private final TransactionStatusFactory transactionStatusFactory;
    private final CardValidationService cardValidationService;

    public CoreTransactionCreateInteractor(CardReadService cardReadService, CommissionService commissionService,
            AccountAllocationService allocationService,
            TransferserviceEventPublisher eventPublisher, TransactionCreateRepository transactionCreateRepository,
            TransactionStatusFactory transactionStatusFactory, CardValidationService cardValidationService) {
        this.cardReadService = cardReadService;
        this.commissionService = commissionService;
        this.allocationService = allocationService;
        this.eventPublisher = eventPublisher;
        this.transactionCreateRepository = transactionCreateRepository;
        this.transactionStatusFactory = transactionStatusFactory;
        this.cardValidationService = cardValidationService;
    }

    @Override
    public TransactionCreateResponse create(TransactionCreateRequest request) throws TransactionException {
        /// Получить карточки
        Card cardFrom = cardReadService.getCardByNumber(request.getCardFromNumber());
        if (cardFrom == null) {
            throw new TransactionException(TransactionExceptionCode.CARD_FROM_NOT_FOUND);
        }
        Card cardTo = cardReadService.getCardByNumber(request.getCardToNumber());
        if (cardTo == null) {
            throw new TransactionException(TransactionExceptionCode.CARD_TO_NOT_FOUND);
        }
        TransactionBuilder builder = new CoreTransactionBuilder();
        builder.setStatus(transactionStatusFactory.create(TransactionStatusCode.NEW))
                .setCardFrom(cardFrom)
                .setCardTo(cardTo)
                .setAmount(request.getAmount())
                .setCurrency(request.getCurrency());

        /// Проверить карточку списания
        CardUserInput userInput = new CoreCardUserInput(request.geCardFromCvv(), request.getCardFromValidTill());
        if (!cardValidationService.isValid(cardFrom, userInput)) {
            throw new TransactionException(TransactionExceptionCode.CARD_FROM_NOT_VALID);
        }

        /// Создать комиссию
        builder.setCommission(commissionService.compute(cardFrom, cardTo, request.getAmount(), request.getCurrency()));

        /// Забронировать сумму на карточке списания
        Transaction transaction = builder.build();
        AccountAllocation allocation = allocationService.allocate(transaction.getId(), cardFrom.getAccountNumber(),
                transaction.getAmountWithCommission());
        if (allocation == null) {
            throw new TransactionException(TransactionExceptionCode.CANNOT_ALLOCATE_SUM);
        }

        /// Сохранить транзакцию
        if (!transactionCreateRepository.store(transaction)) {
            allocationService.delete(allocation);
            throw new TransactionException(TransactionExceptionCode.CANNOT_CREATE_TRANSACTION);
        }

        /// Отправить событие, что транзакция создана: код подтверждения, логирование
        eventPublisher.publish(new TransactionIsCreated(transaction));

        return new CoreTransactionCreateResponse(transaction.getId(), transaction.getStatus());
    }
}
