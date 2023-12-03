package ru.netology.transferservice.core.input;

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
import ru.netology.transferservice.core.factory.CoreTransactionBuilder;

public class CoreTransactionConfirmationInteractor implements TransactionConfirmationInput {
    private final TransactionConfirmRepository transactionConfirmRepository;
    private final ConfirmationConfirmRepository confirmRepository;
    private final TransactionStatusFactory statusFactory;
    private final TransferserviceEventPublisher eventPublisher;

    public CoreTransactionConfirmationInteractor(TransactionConfirmRepository transactionConfirmRepository,
                                                 ConfirmationConfirmRepository confirmRepository,
                                                 TransactionStatusFactory statusFactory,
                                                 TransferserviceEventPublisher eventPublisher
    ) {
        this.transactionConfirmRepository = transactionConfirmRepository;
        this.confirmRepository = confirmRepository;
        this.statusFactory = statusFactory;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public TransactionConfirmationResponse confirm(TransactionConfirmationRequest request) throws TransactionException {
        /// Найти транзакцию
        /// Найти подтверждение
        /// Проверить код подтверждения
        /// Удалить подтверждение
        /// Изменить статус транзакции и обновить транзакцию
        /// Сгенерировать сообщение, что транзакция подтверждена
        /// Вернуть ответ
        Transaction transaction = transactionConfirmRepository.getById(request.getTransactionId());
        if (transaction == null) {
            throw new TransactionException(TransactionExceptionCode.TRANSACTION_NOT_FOUND);
        }
        Confirmation confirmation = confirmRepository.findLastByTransactionId(request.getTransactionId());
        if (confirmation == null) {
            throw new TransactionException(TransactionExceptionCode.CONFIRMATION_NOT_FOUND);
        }
        confirmRepository.delete(confirmation);
        if (!confirmation.getCode().equals(request.getCode())) {
            throw new TransactionException(TransactionExceptionCode.CONFIRMATION_CODE_INVALID);
        }
        Transaction updatedTransaction = new CoreTransactionBuilder().toBuilder(transaction)
                .setStatus(statusFactory.create(TransactionStatusCode.CONFIRMED))
                .build();
        if (!transactionConfirmRepository.update(updatedTransaction)) {
            throw new TransactionException(TransactionExceptionCode.CANNOT_UPDATE_TRANSACTION);
        }

        eventPublisher.publish(new TransactionIsConfirmed(updatedTransaction));

        return new CoreTransactionConfirmationResponse(updatedTransaction.getId(), updatedTransaction.getStatus());
    }
}
