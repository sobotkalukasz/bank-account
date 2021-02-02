package pl.lsobotka.bank.account.service.exception;

public class AccountCreateException extends RuntimeException {
    public AccountCreateException(String message) {
        super(message);
    }
}
