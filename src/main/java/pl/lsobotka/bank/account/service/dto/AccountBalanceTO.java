package pl.lsobotka.bank.account.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record AccountBalanceTO(@JsonProperty("balance")BigDecimal balance) {
}
