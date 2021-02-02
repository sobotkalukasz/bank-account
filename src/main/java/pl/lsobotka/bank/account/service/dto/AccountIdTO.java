package pl.lsobotka.bank.account.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AccountIdTO(@JsonProperty("accountId") Long accountId) {
}
