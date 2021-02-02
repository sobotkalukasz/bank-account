package pl.lsobotka.bank.account.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pl.lsobotka.bank.account.model.Customer;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountRepositoryImplConcurrentTest {

    private AccountRepositoryImpl repository;

    @BeforeEach
    public void initRepository(){
        repository = new AccountRepositoryImpl();
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
    void Should_increaseBalanceAndAddToHistory_When_concurrentCreateAccount(int users, int threads) {
        ConcurrentLinkedQueue<Long> ids = new ConcurrentLinkedQueue<>();
        Runnable createUser = () -> {
            Long id = repository.save(new Customer("Concurrent", "User"));
            ids.add(id);
        };
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        IntStream.range(0, users)
                .forEach(i -> executor.submit(createUser));
        stop(executor);
        assertThat(ids.stream().distinct().count()).isEqualTo(users);
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
