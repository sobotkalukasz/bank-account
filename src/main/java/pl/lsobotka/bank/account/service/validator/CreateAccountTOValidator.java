package pl.lsobotka.bank.account.service.validator;

import pl.lsobotka.bank.account.service.dto.CreateAccountTO;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.IntPredicate;

public class CreateAccountTOValidator extends AbstractValidator<CreateAccountTO> {

    private static final IntPredicate isAlphabetic = Character::isAlphabetic;

    private final Function<CreateAccountTO, Optional<String>> isNull = toValidate ->
            Objects.isNull(toValidate) ? Optional.of("CreateAccount.empty") : Optional.empty();

    private final Function<CreateAccountTO, Optional<String>> isFirstNameNullOrEmpty = toValidate -> {
        if (Objects.nonNull(toValidate) && (Objects.isNull(toValidate.firstName()) || toValidate.firstName().isBlank()))
            return Optional.of("CreateAccount.empty");
        return Optional.empty();
    };

    private final Function<CreateAccountTO, Optional<String>> isLastNameNullOrEmpty = toValidate -> {
        if (Objects.nonNull(toValidate) && (Objects.isNull(toValidate.lastName()) || toValidate.lastName().isBlank()))
            return Optional.of("CreateAccount.empty");
        return Optional.empty();
    };

    private final Function<CreateAccountTO, Optional<String>> isFirstNameAlpha = toValidate -> {
        if (Objects.nonNull(toValidate) && (Objects.nonNull(toValidate.firstName())
                && toValidate.firstName().chars().anyMatch(isAlphabetic.negate())))
            return Optional.of("CreateAccount.onlyAlpha");
        return Optional.empty();
    };

    private final Function<CreateAccountTO, Optional<String>> isLastNameAlpha = toValidate -> {
        if (Objects.nonNull(toValidate) && (Objects.nonNull(toValidate.lastName())
                && toValidate.lastName().chars().anyMatch(isAlphabetic.negate())))
            return Optional.of("CreateAccount.onlyAlpha");
        return Optional.empty();
    };

    public static ValidationResult validateObject(CreateAccountTO toValidate) {
        return new CreateAccountTOValidator().validate(toValidate);
    }

    @Override
    protected List<Function<CreateAccountTO, Optional<String>>> getValidators() {
        return List.of(isNull, isFirstNameNullOrEmpty, isLastNameNullOrEmpty, isFirstNameAlpha, isLastNameAlpha);
    }

}
