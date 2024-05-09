package com.techbank.account.query.api.controllers;

import com.techbank.account.query.api.dto.AccountLookupResponse;
import com.techbank.account.query.api.dto.EqualityType;
import com.techbank.account.query.api.queries.FindAccountByHolderQuery;
import com.techbank.account.query.api.queries.FindAccountByIdQuery;
import com.techbank.account.query.api.queries.FindAccountWithBalanceQuery;
import com.techbank.account.query.api.queries.FindAllAccountsQuery;
import com.techbank.cqrs.core.queries.QueryDispatcher;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/bankAccountLookup")
public class AccountLookupController {

    private final QueryDispatcher queryDispatcher;

    @GetMapping
    public ResponseEntity<AccountLookupResponse> getAllAccounts() {
        var response = AccountLookupResponse.builder()
                .bankAccounts(queryDispatcher.send(new FindAllAccountsQuery()))
                .build();
        return new ResponseEntity<>(response, OK);
    }

    //TODO fucking piece of shit
    @GetMapping(path = "/{id}")
    public ResponseEntity<AccountLookupResponse> getAccountById(@PathVariable String id) {
        var response = AccountLookupResponse.builder()
                .bankAccounts(queryDispatcher.send(new FindAccountByIdQuery(id)))
                .build();
        return new ResponseEntity<>(response, OK);
    }

    //TODO fucking piece of shit II
    @GetMapping(path = "/byHolder/{holder}")
    public ResponseEntity<AccountLookupResponse> getAccountByHolder(@PathVariable String holder) {
        var response = AccountLookupResponse.builder()
                .bankAccounts(queryDispatcher.send(new FindAccountByHolderQuery(holder)))
                .build();
        return new ResponseEntity<>(response, OK);
    }

    //TODO fucking piece of shit III
    @GetMapping(path = "/{equalityType}/{balance}")
    public ResponseEntity<AccountLookupResponse> getAccountWithBalance(@PathVariable EqualityType equalityType,
                                                                       @PathVariable BigDecimal balance) {
        var response = AccountLookupResponse.builder()
                .bankAccounts(queryDispatcher.send(new FindAccountWithBalanceQuery(equalityType, balance)))
                .build();
        return new ResponseEntity<>(response, OK);
    }

}
