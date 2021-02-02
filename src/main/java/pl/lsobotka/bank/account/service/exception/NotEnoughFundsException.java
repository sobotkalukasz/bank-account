package pl.lsobotka.bank.account.service.exception;

public class NotEnoughFundsException extends OperationException {
    public NotEnoughFundsException(String message) {
        super(message);
    }
}
