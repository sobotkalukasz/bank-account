package pl.lsobotka.bank.account.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pl.lsobotka.bank.account.service.exception.NotEnoughFundsException;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class AccountTest {

    private Account account;

    @BeforeEach
    public void initAccount() {
        account = new Account(1L, new Customer("Test", "User"));
    }

    @Test
    void Should_increaseBalance_When_deposit() {
        BigDecimal deposit = BigDecimal.valueOf(125.15);
        account.deposit(deposit);
        assertThat(account.getBalance()).isEqualTo(deposit);
    }

    @Test
    void Should_decreaseBalance_When_withdraw() {
        BigDecimal startingBalance = BigDecimal.valueOf(200);
        BigDecimal withdrawValue = BigDecimal.valueOf(50);

        account.deposit(startingBalance);
        assertThat(account.getBalance()).isEqualTo(startingBalance);

        account.withdraw(withdrawValue);
        assertThat(account.getBalance()).isEqualTo(startingBalance.subtract(withdrawValue));
    }

    private static Stream<Arguments> operations() {
        return Stream.of(
                Arguments.of(DoubleStream.of(139.25, 26.38, 1059.50, 256.56).mapToObj(BigDecimal::valueOf).collect(Collectors.toList()))
        );
    }

    @ParameterizedTest()
    @MethodSource("operations")
    void Should_createHistory_When_depositAndWithdraw(List<BigDecimal> operations) {
        operations.forEach(op -> {
            account.deposit(op);
            account.withdraw(op);
        });
        assertThat(account.getHistory().size()).isEqualTo(operations.size() * 2);
        double actualHistoryBalance = account.getHistory().stream().map(OperationHistoryEntry::value).mapToDouble(BigDecimal::doubleValue).sum();
        assertThat(actualHistoryBalance).isEqualTo(0);
    }

    @Test
    void Should_thrownNotEnoughFundsException_When_withdrawNotEnoughFunds() {
        assertThatExceptionOfType(NotEnoughFundsException.class)
                .isThrownBy(() -> account.withdraw(BigDecimal.TEN));
    }

}
