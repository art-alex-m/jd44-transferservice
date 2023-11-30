package ru.netology.transferservice.core.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import ru.netology.transferservice.contracts.entity.TransactionStatus;
import ru.netology.transferservice.contracts.entity.TransactionStatusCode;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Builder
public class CoreTransactionStatus implements TransactionStatus {
    private final UUID id;
    private final TransactionStatusCode code;
    private final String message;
    private final LocalDateTime createdAt;
}
