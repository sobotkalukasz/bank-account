package pl.lsobotka.bank.account.service.validator;

import java.util.Collection;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public class ValidationResult {

    private Deque<String> errors;

    public static ValidationResult of(Collection<String> errors) {
        return new ValidationResult(errors);
    }

    private ValidationResult(Collection<String> errors) {
        this.errors = new LinkedList<>(errors);
    }

    public boolean isValid() {
        return errors.isEmpty();
    }

    public boolean isNotValid() {
        return !isValid();
    }

    public List<String> getErrors() {
        return List.copyOf(errors);
    }

    public String getFirstError() {
        return errors.isEmpty() ? "" : errors.getFirst();
    }

    public <X extends RuntimeException> void ifNotValidThrow(Supplier<? extends X> exceptionSupplier) {
        if (isNotValid())
            throw exceptionSupplier.get();
    }

}
