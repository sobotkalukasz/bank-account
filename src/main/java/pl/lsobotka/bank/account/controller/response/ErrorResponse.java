package pl.lsobotka.bank.account.controller.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Builder
@Getter
public class ErrorResponse {
    private final HttpStatus status;
    private final String message;
}
