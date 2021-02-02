package pl.lsobotka.bank.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.lsobotka.bank.account.service.AccountService;
import pl.lsobotka.bank.account.service.dto.AccountBalanceTO;
import pl.lsobotka.bank.account.service.dto.AccountIdTO;
import pl.lsobotka.bank.account.service.dto.CreateAccountTO;
import pl.lsobotka.bank.account.service.dto.OperationTO;
import pl.lsobotka.bank.account.service.exception.AccountNotFoundException;
import pl.lsobotka.bank.account.service.exception.OperationException;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService service;

    @Test
    void Should_createAccount_When_validCreateAccountTO() throws Exception {
        CreateAccountTO testUserTO = new CreateAccountTO("Test", "User");
        AccountIdTO accountIdTO = new AccountIdTO(1L);
        when(service.createAccount(testUserTO)).thenReturn(accountIdTO);

        mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUserTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(accountIdTO)));
    }

    @Test
    void Should_getBalance_When_accountExist() throws Exception {
        AccountBalanceTO balanceTO = new AccountBalanceTO(BigDecimal.valueOf(256.56));
        when(service.getBalance(1L)).thenReturn(balanceTO);

        mockMvc.perform(get("/accounts/1/balance"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(balanceTO)));
    }

    @Test
    void Should_deposit_When_accountExist() throws Exception {
        OperationTO operationTO = new OperationTO(BigDecimal.valueOf(256.56));

        mockMvc.perform(put("/accounts/1/deposit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(operationTO)))
                .andExpect(status().isNoContent());
    }

    @Test
    void Should_withdraw_When_accountExist() throws Exception {
        OperationTO operationTO = new OperationTO(BigDecimal.valueOf(256.56));

        mockMvc.perform(put("/accounts/1/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(operationTO)))
                .andExpect(status().isNoContent());
    }

    @Test
    void Should_notFound_When_accountNotExist() throws Exception {
        when(service.getBalance(1L)).thenThrow(new AccountNotFoundException("exception message"));

        mockMvc.perform(get("/accounts/1/balance"))
                .andExpect(status().isNotFound())
                .andExpect(
                        result -> assertThat(result.getResolvedException()).isExactlyInstanceOf(AccountNotFoundException.class))
                .andExpect(content().json("""
                        {
                            "status": "NOT_FOUND",
                            "message": "exception message"
                        }
                        """));
    }

    @Test
    void Should_operationException_When_invalidOperationTO() throws Exception {
        int value = -256;
        OperationTO operationTO = new OperationTO(BigDecimal.valueOf(value));
        doThrow(new OperationException("exception message"))
                .when(service).deposit(1L, operationTO);

        mockMvc.perform(put("/accounts/1/deposit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(operationTO)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(result ->
                        assertThat(result.getResolvedException()).isExactlyInstanceOf(OperationException.class))
                .andExpect(content().json("""
                        {
                            "status": "UNPROCESSABLE_ENTITY",
                            "message": "exception message"
                        }
                        """));
    }

}
