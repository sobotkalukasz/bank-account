package pl.lsobotka.bank.account.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.lsobotka.bank.account.service.AccountService;
import pl.lsobotka.bank.account.service.dto.AccountBalanceTO;
import pl.lsobotka.bank.account.service.dto.AccountIdTO;
import pl.lsobotka.bank.account.service.dto.CreateAccountTO;
import pl.lsobotka.bank.account.service.dto.OperationTO;

@RestController
@RequestMapping(path = "/accounts")
public class AccountController {

    @Autowired
    private AccountService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    AccountIdTO create(@RequestBody CreateAccountTO dto) {
        return service.createAccount(dto);
    }

    @GetMapping("/{id}/balance")
    @ResponseStatus(HttpStatus.OK)
    AccountBalanceTO getBalance(@PathVariable Long id) {
        return service.getBalance(id);
    }

    @PutMapping("/{id}/deposit")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deposit(@PathVariable Long id, @RequestBody OperationTO operation) {
        service.deposit(id, operation);
    }

    @PutMapping("/{id}/withdraw")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void withdraw(@PathVariable Long id, @RequestBody OperationTO operation) {
        service.withdraw(id, operation);
    }

}
