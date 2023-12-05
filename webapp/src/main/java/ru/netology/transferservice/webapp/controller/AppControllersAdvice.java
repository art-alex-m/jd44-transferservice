package ru.netology.transferservice.webapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.netology.transferservice.contracts.exception.TransferserviceException;
import ru.netology.transferservice.webapp.model.AppError;

@RestControllerAdvice
public class AppControllersAdvice {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AppError handleValidationException(MethodArgumentNotValidException ex) {
        FieldError error = (FieldError) ex.getBindingResult().getAllErrors().get(0);
        return new AppError(0, error.getDefaultMessage());
    }

    @ExceptionHandler(TransferserviceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AppError handleTransactionException(TransferserviceException ex) {
        return new AppError(ex.getId(), ex.getLocalizedMessage());
    }
}
