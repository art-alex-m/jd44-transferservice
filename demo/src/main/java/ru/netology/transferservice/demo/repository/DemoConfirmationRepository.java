package ru.netology.transferservice.demo.repository;

import lombok.extern.slf4j.Slf4j;
import ru.netology.transferservice.contracts.entity.Confirmation;
import ru.netology.transferservice.contracts.repository.ConfirmationConfirmRepository;
import ru.netology.transferservice.contracts.repository.ConfirmationCreateRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
public class DemoConfirmationRepository implements ConfirmationConfirmRepository, ConfirmationCreateRepository {

    private final Map<UUID, Confirmation> storage = new HashMap<>();

    @Override
    public Confirmation findLastByTransactionId(UUID transactionId) {
        return storage.get(transactionId);
    }

    @Override
    public boolean delete(Confirmation confirmation) {
        storage.remove(confirmation.getTransactionId());
        return true;
    }

    @Override
    public boolean store(Confirmation confirmation) {
        storage.put(confirmation.getTransactionId(), confirmation);
        log.info("operationId=\"{}\", code=\"{}\"", confirmation.getTransactionId().toString(), confirmation.getCode());
        return true;
    }
}
