package com.techbank.account.cmd.configuration;

import com.techbank.account.cmd.api.commands.CloseAccountCommand;
import com.techbank.account.cmd.api.commands.CommandHandler;
import com.techbank.account.cmd.api.commands.DepositFundsCommand;
import com.techbank.account.cmd.api.commands.OpenAccountCommand;
import com.techbank.account.cmd.api.commands.WithdrawFundsCommand;
import com.techbank.cqrs.core.infrastructure.CommandDispatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CommandHandlerRegistration {

    private final CommandDispatcher commandDispatcher;
    private final CommandHandler commandHandler;

    @PostConstruct
    public void init() {
        commandDispatcher.registerHandler(OpenAccountCommand.class, commandHandler::handle);
        commandDispatcher.registerHandler(CloseAccountCommand.class, commandHandler::handle);
        commandDispatcher.registerHandler(DepositFundsCommand.class, commandHandler::handle);
        commandDispatcher.registerHandler(WithdrawFundsCommand.class, commandHandler::handle);

        log.info("Registered command handlers");
    }

}
