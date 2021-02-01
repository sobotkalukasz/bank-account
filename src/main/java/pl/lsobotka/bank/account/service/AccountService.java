package pl.lsobotka.bank.account.service;

import pl.lsobotka.bank.account.service.dto.AccountBalanceTO;
import pl.lsobotka.bank.account.service.dto.AccountIdTO;
import pl.lsobotka.bank.account.service.dto.CreateAccountTO;
import pl.lsobotka.bank.account.service.dto.OperationTO;

public interface AccountService {

    AccountIdTO createAccount(CreateAccountTO dto);
    AccountBalanceTO getBalance(Long id);
    void deposit(Long id, OperationTO dto);
    void withdraw(Long id, OperationTO dto);
}
