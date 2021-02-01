package pl.lsobotka.bank.account.repository;

import pl.lsobotka.bank.account.model.Account;
import pl.lsobotka.bank.account.model.Customer;

import java.util.Optional;

public interface AccountRepository {
    Long save(Customer customer);

    Optional<Account> findById(Long id);
}
