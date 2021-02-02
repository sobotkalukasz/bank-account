package pl.lsobotka.bank.account.service.exception;

public class OperationException extends RuntimeException {
    public OperationException(String message) {
        super(message);
    }
}
