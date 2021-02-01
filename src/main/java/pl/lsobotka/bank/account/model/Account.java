package pl.lsobotka.bank.account.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class Account {

    private Long id;
    private Customer customer;
    private BigDecimal balance = BigDecimal.ZERO;
    private List<OperationHistoryEntry> history = new ArrayList<>();

    public Account(Long id, Customer customer) {
        this.customer = customer;
    }

    public Long getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Collection<OperationHistoryEntry> getHistory() {
        return List.copyOf(history);
    }

    public synchronized void deposit(BigDecimal amount) {
        addToHistory(amount);
        balance = balance.add(amount);
    }

    public synchronized void withdraw(BigDecimal amount) {
        addToHistory(amount.negate());
        balance = balance.subtract(amount);
    }

    private void addToHistory(BigDecimal amount) {
        history.add(new OperationHistoryEntry(LocalDateTime.now(), amount));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(id, account.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
