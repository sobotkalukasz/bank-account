package pl.lsobotka.bank.account.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountConcurrentTest {

    private Account account;

    @BeforeEach
    public void initAccount() {
        account = new Account(1L, new Customer("Concurrent", "User"));
    }

    private static Stream<Arguments> concurrentParams() {
        return Stream.of(
                Arguments.of(1000, 2),
                Arguments.of(10_000, 5),
                Arguments.of(100_000, 10)
        );
    }

    @ParameterizedTest
    @MethodSource("concurrentParams")
    void Should_increaseBalanceAndAddToHistory_When_concurrentDeposit(int times, int threads) {
        Runnable deposit = () -> account.deposit(BigDecimal.TEN);
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        IntStream.range(0, times)
                .forEach(i -> executor.submit(deposit));
        stop(executor);

        assertThat(account.getBalance()).isEqualTo(BigDecimal.TEN.multiply(BigDecimal.valueOf(times)));
        assertThat(account.getHistory().size()).isEqualTo(times);
    }

    @ParameterizedTest
    @MethodSource("concurrentParams")
    void Should_decreaseBalanceAndAddToHistory_When_concurrentWithdraw(int times, int threads) {
        account.deposit(BigDecimal.valueOf(10 * times));
        Runnable withdraw = () -> account.withdraw(BigDecimal.TEN);
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        IntStream.range(0, times)
                .forEach(i -> executor.submit(withdraw));
        stop(executor);

        assertThat(account.getBalance()).isEqualTo(BigDecimal.ZERO);
        assertThat(account.getHistory().size()).isEqualTo(times + 1);// with initial deposit
    }

    private static void stop(ExecutorService executor) {
        try {
            executor.shutdown();
            executor.awaitTermination(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.err.println("termination interrupted");
        } finally {
            if (!executor.isTerminated()) {
                System.err.println("killing non-finished tasks");
            }
            executor.shutdownNow();
        }
    }
}
