package pl.lsobotka.bank.account.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OperationHistoryEntry(LocalDateTime timestamp, BigDecimal value) {
}
