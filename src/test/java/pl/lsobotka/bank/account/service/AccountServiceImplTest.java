package pl.lsobotka.bank.account.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.lsobotka.bank.account.model.Account;
import pl.lsobotka.bank.account.model.Customer;
import pl.lsobotka.bank.account.repository.AccountRepositoryImpl;
import pl.lsobotka.bank.account.service.dto.AccountIdTO;
import pl.lsobotka.bank.account.service.dto.CreateAccountTO;
import pl.lsobotka.bank.account.service.dto.OperationTO;
import pl.lsobotka.bank.account.service.exception.AccountCreateException;
import pl.lsobotka.bank.account.service.exception.AccountNotFoundException;
import pl.lsobotka.bank.account.service.exception.NotEnoughFundsException;
import pl.lsobotka.bank.account.service.exception.OperationException;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {

    @Mock
    private AccountRepositoryImpl repository;

    @InjectMocks
    private static AccountServiceImpl service;

    @Test
    void Should_createAccount_When_validCreateAccountTO() {
        Customer customer = new Customer("Valid", "User");
        when(repository.save(customer)).thenReturn(1L);

        AccountIdTO idTO = service.createAccount(new CreateAccountTO("Valid", "User"));
        assertThat(idTO.accountId()).isEqualTo(1L);
    }

    private static Stream<Arguments> invalidCreateAccountTO() {
        return Stream.of(
                Arguments.of(new Object[]{null}),
                Arguments.of(new CreateAccountTO("", "")),
                Arguments.of(new CreateAccountTO("valid", "")),
                Arguments.of(new CreateAccountTO("", "valid")),
                Arguments.of(new CreateAccountTO("invalid2", "valid")),
                Arguments.of(new CreateAccountTO("valid", "invalid@"))
        );
    }

    @ParameterizedTest
    @MethodSource("invalidCreateAccountTO")
    void Should_thrownAccountCreateException_When_createAccountTOisNotValid(CreateAccountTO accountTO) {
        assertThatExceptionOfType(AccountCreateException.class)
                .isThrownBy(() -> service.createAccount(accountTO));
    }

    @Test
    void Should_thrownAccountNotFoundException_When_getBalanceAccountNotExist() {
        assertThatExceptionOfType(AccountNotFoundException.class)
                .isThrownBy(() -> service.getBalance(1L));
    }

    @Test
    void Should_thrownAccountNotFoundException_When_depositAccountNotExist() {
        assertThatExceptionOfType(AccountNotFoundException.class)
                .isThrownBy(() -> service.deposit(1L, new OperationTO(BigDecimal.TEN)));
    }

    @Test
    void Should_thrownAccountNotFoundException_When_withdrawAccountNotExist() {
        assertThatExceptionOfType(AccountNotFoundException.class)
                .isThrownBy(() -> service.withdraw(1L, new OperationTO(BigDecimal.TEN)));
    }

    @Test
    void Should_thrownNotEnoughFundsException_When_withdrawNotEnoughFunds() {
        when(repository.findById(1L)).thenReturn(Optional.of(new Account(1L, new Customer("Tester", "Tester"))));
        assertThatExceptionOfType(NotEnoughFundsException.class)
                .isThrownBy(() -> service.withdraw(1L, new OperationTO(BigDecimal.TEN)));
    }

    private static Stream<Arguments> invalidOperationTO() {
        return Stream.of(
                Arguments.of(new Object[]{null}),
                Arguments.of(new OperationTO(null)),
                Arguments.of(new OperationTO(BigDecimal.ZERO)),
                Arguments.of(new OperationTO(BigDecimal.valueOf(-5)))
        );
    }

    @ParameterizedTest
    @MethodSource("invalidOperationTO")
    void Should_thrownOperationException_When_depositInvalidOperation(OperationTO operationTO) {
        assertThatExceptionOfType(OperationException.class)
                .isThrownBy(() -> service.deposit(1L, operationTO));
    }

    @ParameterizedTest
    @MethodSource("invalidOperationTO")
    void Should_thrownOperationException_When_withdrawInvalidOperation(OperationTO operationTO) {
        assertThatExceptionOfType(OperationException.class)
                .isThrownBy(() -> service.withdraw(1L, operationTO));
    }

}