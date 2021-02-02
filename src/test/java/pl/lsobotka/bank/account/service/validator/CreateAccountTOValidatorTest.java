package pl.lsobotka.bank.account.service.validator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pl.lsobotka.bank.account.service.dto.CreateAccountTO;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class CreateAccountTOValidatorTest {

    @Test
    void Should_passValidation_When_validCreateAccountTO() {
        CreateAccountTO accountTO = new CreateAccountTO("Valid", "User");
        ValidationResult result = CreateAccountTOValidator.validateObject(accountTO);
        assertThat(result.isValid()).isTrue();
        assertThat(result.getErrors()).isEmpty();
    }

    private static Stream<Arguments> invalidCreateAccountTO() {
        return Stream.of(
                Arguments.of(null, List.of("CreateAccount.empty")),
                Arguments.of(new CreateAccountTO("", ""), List.of("CreateAccount.empty", "CreateAccount.empty")),
                Arguments.of(new CreateAccountTO("valid", ""), List.of("CreateAccount.empty")),
                Arguments.of(new CreateAccountTO("", "valid"), List.of("CreateAccount.empty")),
                Arguments.of(new CreateAccountTO("invalid2", "valid"), List.of("CreateAccount.onlyAlpha")),
                Arguments.of(new CreateAccountTO("valid", "invalid@"), List.of("CreateAccount.onlyAlpha"))
        );
    }

    @ParameterizedTest()
    @MethodSource("invalidCreateAccountTO")
    void Should_notPassValidation_When_invalidCreateAccountTO(CreateAccountTO toValidate, List<String> expectedErrors) {
        ValidationResult result = CreateAccountTOValidator.validateObject(toValidate);
        assertThat(result.isNotValid()).isTrue();
        assertThat(result.getErrors()).isNotEmpty();
        assertThat(result.getErrors()).hasSize(expectedErrors.size());
    }
}
