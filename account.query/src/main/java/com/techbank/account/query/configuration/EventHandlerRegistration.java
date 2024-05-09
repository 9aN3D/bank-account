package com.techbank.account.query.configuration;

import com.techbank.account.common.events.AccountClosedEvent;
import com.techbank.account.common.events.AccountOpenedEvent;
import com.techbank.account.common.events.FundsDepositedEvent;
import com.techbank.account.common.events.FundsWithdrawnEvent;
import com.techbank.account.query.infrastructure.handlers.EventHandler;
import com.techbank.cqrs.core.infrastructure.EventDispatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class EventHandlerRegistration {

    private final EventDispatcher eventDispatcher;
    private final EventHandler eventHandler;

    @PostConstruct
    public void init() {
        eventDispatcher.registerHandler(AccountOpenedEvent.class, eventHandler::on);
        eventDispatcher.registerHandler(AccountClosedEvent.class, eventHandler::on);
        eventDispatcher.registerHandler(FundsDepositedEvent.class, eventHandler::on);
        eventDispatcher.registerHandler(FundsWithdrawnEvent.class, eventHandler::on);

        log.info("Registered event handlers");
    }

}
