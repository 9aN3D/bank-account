package com.techbank.account.query.api.exceptions;

import com.techbank.account.query.api.queries.FindAccountByHolderQuery;
import com.techbank.account.query.api.queries.FindAccountByIdQuery;

public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(String message) {
        super(message);
    }

    public AccountNotFoundException(FindAccountByHolderQuery query) {
        this("Account not found by holder: " + query.getAccountHolder());
    }

    public AccountNotFoundException(FindAccountByIdQuery query) {
        this("Account not found by id: " + query.getId());
    }

}
