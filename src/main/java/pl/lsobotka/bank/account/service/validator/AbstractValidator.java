package pl.lsobotka.bank.account.service.validator;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class AbstractValidator<T> {

    protected abstract List<Function<T, Optional<String>>> getValidators();

    protected ValidationResult validate(T t) {
        var validators = getValidators();
        Collection<String> errors = validators.stream()
                .map(validator -> validator.apply(t))
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
        return ValidationResult.of(errors);
    }
}
