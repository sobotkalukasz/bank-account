package pl.lsobotka.bank.account.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateAccountTO(
        @JsonProperty("firstName") String firstName,
        @JsonProperty("lastName") String lastName) {
}
