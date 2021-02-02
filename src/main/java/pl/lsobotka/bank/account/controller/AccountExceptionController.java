package pl.lsobotka.bank.account.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.lsobotka.bank.account.controller.response.ErrorResponse;
import pl.lsobotka.bank.account.service.exception.AccountCreateException;
import pl.lsobotka.bank.account.service.exception.AccountNotFoundException;
import pl.lsobotka.bank.account.service.exception.OperationException;

@RestControllerAdvice
public class AccountExceptionController {

    @ExceptionHandler(AccountNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse AccountNotFoundExceptionHandler(AccountNotFoundException ex) {
        return ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND)
                .message(ex.getMessage()).build();
    }

    @ExceptionHandler(OperationException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorResponse OperationExceptionHandler(OperationException ex) {
        return ErrorResponse.builder()
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .message(ex.getMessage()).build();
    }

    @ExceptionHandler(AccountCreateException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorResponse AccountCreateExceptionHandler(AccountCreateException ex) {
        return ErrorResponse.builder()
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .message(ex.getMessage()).build();
    }
}
