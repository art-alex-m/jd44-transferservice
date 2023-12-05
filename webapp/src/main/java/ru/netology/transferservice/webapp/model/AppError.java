package ru.netology.transferservice.webapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AppError {
    private int id;
    private String message;
}
