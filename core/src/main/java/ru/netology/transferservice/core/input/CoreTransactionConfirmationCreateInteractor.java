package ru.netology.transferservice.core.input;

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

public class CoreTransactionConfirmationCreateInteractor implements TransactionConfirmationCreateInput {

    private final ConfirmationFactory confirmationFactory;
    private final ConfirmationCreateRepository confirmationCreateRepository;

    private final TransferserviceEventPublisher eventPublisher;

    public CoreTransactionConfirmationCreateInteractor(ConfirmationFactory confirmationFactory,
            ConfirmationCreateRepository confirmationCreateRepository, TransferserviceEventPublisher eventPublisher) {
        this.confirmationFactory = confirmationFactory;
        this.confirmationCreateRepository = confirmationCreateRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public TransactionConfirmationCreateResponse create(TransactionConfirmationCreateRequest request)
            throws TransactionException {
        Confirmation confirmation = confirmationFactory.create(request.getTransactionId());
        if (confirmation == null || !confirmationCreateRepository.store(confirmation)) {
            throw new TransactionException(TransactionExceptionCode.CANNOT_CREATE_CONFIRMATION);
        }

        eventPublisher.publish(new ConfirmationIsCreated(confirmation));

        return new CoreTransactionConfirmationCreateResponse(confirmation);
    }
}
