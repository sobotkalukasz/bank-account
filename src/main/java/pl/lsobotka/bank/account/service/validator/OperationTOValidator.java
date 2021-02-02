package pl.lsobotka.bank.account.service.validator;

import pl.lsobotka.bank.account.service.dto.OperationTO;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public class OperationTOValidator extends AbstractValidator<OperationTO> {

    private final Function<OperationTO, Optional<String>> isNull = toValidate -> {
        if (Objects.isNull(toValidate) || (Objects.isNull(toValidate.amount())))
            return Optional.of("Operation.empty");
        return Optional.empty();
    };

    private final Function<OperationTO, Optional<String>> isGreaterThanZero = toValidate -> {
        if (isNull.apply(toValidate).isEmpty() && toValidate.amount().signum() <= 0)
            return Optional.of("Operation.valueGt");
        return Optional.empty();
    };

    public static ValidationResult validateObject(OperationTO toValidate) {
        return new OperationTOValidator().validate(toValidate);
    }

    @Override
    protected List<Function<OperationTO, Optional<String>>> getValidators() {
        return List.of(isNull, isGreaterThanZero);
    }
}
