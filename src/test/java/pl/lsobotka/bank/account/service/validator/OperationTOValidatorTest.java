package pl.lsobotka.bank.account.service.validator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pl.lsobotka.bank.account.service.dto.OperationTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class OperationTOValidatorTest {

    @Test
    void Should_passValidation_When_validOperationTO() {
        OperationTO operationTO = new OperationTO(BigDecimal.TEN);
        ValidationResult result = OperationTOValidator.validateObject(operationTO);
        assertThat(result.isValid()).isTrue();
        assertThat(result.getErrors()).isEmpty();
    }

    private static Stream<Arguments> invalidOperations() {
        return Stream.of(
                Arguments.of(null, List.of("Operation.empty")),
                Arguments.of(new OperationTO(null), List.of("Operation.empty")),
                Arguments.of(new OperationTO(BigDecimal.ZERO), List.of("Operation.valueGt")),
                Arguments.of(new OperationTO(BigDecimal.valueOf(-5)), List.of("Operation.valueGt"))
        );
    }

    @ParameterizedTest()
    @MethodSource("invalidOperations")
    void Should_notPassValidation_When_invalidOperationTO(OperationTO toValidate, List<String> expectedErrors) {
        ValidationResult result = OperationTOValidator.validateObject(toValidate);
        assertThat(result.isNotValid()).isTrue();
        assertThat(result.getErrors()).isNotEmpty();
        assertThat(result.getErrors()).hasSize(expectedErrors.size());
    }
}
