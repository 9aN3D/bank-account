package com.techbank.account.query.configuration;

import com.techbank.account.query.api.queries.FindAccountByHolderQuery;
import com.techbank.account.query.api.queries.FindAccountByIdQuery;
import com.techbank.account.query.api.queries.FindAccountWithBalanceQuery;
import com.techbank.account.query.api.queries.FindAllAccountsQuery;
import com.techbank.account.query.api.queries.QueryHandler;
import com.techbank.cqrs.core.queries.QueryDispatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class QueryHandlerRegistration {

    private final QueryDispatcher queryDispatcher;
    private final QueryHandler queryHandler;

    @PostConstruct
    public void init() {
        queryDispatcher.registerHandler(FindAllAccountsQuery.class, queryHandler::handle);
        queryDispatcher.registerHandler(FindAccountByIdQuery.class, queryHandler::handle);
        queryDispatcher.registerHandler(FindAccountWithBalanceQuery.class, queryHandler::handle);
        queryDispatcher.registerHandler(FindAccountByHolderQuery.class, queryHandler::handle);

        log.info("Registered query handlers");
    }

}
