package pl.lsobotka.bank.account.repository;

import org.springframework.stereotype.Repository;
import pl.lsobotka.bank.account.model.Account;
import pl.lsobotka.bank.account.model.Customer;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class AccountRepositoryImpl implements AccountRepository {

    private static Map<Long, Account> accounts = new ConcurrentHashMap<>();
    private static AtomicLong accountIdStrategy = new AtomicLong();

    @Override
    public Long save(Customer customer) {
        long id = accountIdStrategy.incrementAndGet();
        accounts.put(id, new Account(id, customer));
        return id;
    }

    @Override
    public Optional<Account> findById(Long id) {
        return Optional.ofNullable(accounts.get(id));
    }
}
