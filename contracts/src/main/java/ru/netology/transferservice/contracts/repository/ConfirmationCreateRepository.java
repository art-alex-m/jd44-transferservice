package ru.netology.transferservice.contracts.repository;

import ru.netology.transferservice.contracts.entity.Confirmation;

public interface ConfirmationCreateRepository {
    boolean store(Confirmation confirmation);
}
