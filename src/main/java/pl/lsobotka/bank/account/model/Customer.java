package pl.lsobotka.bank.account.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class Customer {

    private final String firstName;
    private final String lastName;

}
