package pl.lsobotka.bank.account.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lsobotka.bank.account.model.Account;
import pl.lsobotka.bank.account.model.Customer;
import pl.lsobotka.bank.account.repository.AccountRepository;
import pl.lsobotka.bank.account.service.dto.AccountBalanceTO;
import pl.lsobotka.bank.account.service.dto.AccountIdTO;
import pl.lsobotka.bank.account.service.dto.CreateAccountTO;
import pl.lsobotka.bank.account.service.dto.OperationTO;
import pl.lsobotka.bank.account.service.exception.AccountCreateException;
import pl.lsobotka.bank.account.service.exception.AccountNotFoundException;
import pl.lsobotka.bank.account.service.exception.NotEnoughFundsException;
import pl.lsobotka.bank.account.service.exception.OperationException;
import pl.lsobotka.bank.account.service.validator.CreateAccountTOValidator;
import pl.lsobotka.bank.account.service.validator.OperationTOValidator;
import pl.lsobotka.bank.account.service.validator.ValidationResult;

import java.math.BigDecimal;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository repository;

    @Override
    public AccountIdTO createAccount(CreateAccountTO accountTO) {
        ValidationResult validationResult = CreateAccountTOValidator.validateObject(accountTO);
        validationResult.ifNotValidThrow(() -> new AccountCreateException(validationResult.getFirstError()));

        Long accountId = repository.save(new Customer(accountTO.firstName(), accountTO.lastName()));
        return new AccountIdTO(accountId);
    }

    @Override
    public AccountBalanceTO getBalance(Long id) {
        return new AccountBalanceTO(getAccountById(id).getBalance());
    }

    @Override
    public void deposit(Long id, OperationTO operation) {
        ValidationResult validationResult = OperationTOValidator.validateObject(operation);
        validationResult.ifNotValidThrow(() -> new OperationException(validationResult.getFirstError()));

        getAccountById(id).deposit(operation.amount());
    }

    @Override
    public void withdraw(Long id, OperationTO operation) {
        ValidationResult validationResult = OperationTOValidator.validateObject(operation);
        validationResult.ifNotValidThrow(() -> new OperationException(validationResult.getFirstError()));

        Account account = getAccountById(id);
        account.withdraw(operation.amount());
    }

    private Account getAccountById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account.notFound"));
    }

}
