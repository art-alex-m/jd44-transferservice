package ru.netology.transferservice.core.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.netology.transferservice.contracts.entity.Confirmation;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class CoreConfirmation implements Confirmation {
    private final String code;
    private final UUID transactionId;
}
