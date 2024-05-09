package com.techbank.account.cmd.api.commands;

import com.techbank.account.cmd.domain.AccountAggregate;
import com.techbank.cqrs.core.handlers.EventSourcingHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountCommandHandler implements CommandHandler {

    private final EventSourcingHandler<AccountAggregate> eventSourcingHandler;

    @Override
    public void handle(OpenAccountCommand command) {
        log.trace("Receiving OpenAccountCommand {command: {}}", command);

        var aggregate = new AccountAggregate(command);
        eventSourcingHandler.save(aggregate);

        log.info("Received OpenAccountCommand {command: {}}", command);
    }

    @Override
    public void handle(CloseAccountCommand command) {
        log.trace("Receiving CloseAccountCommand {command: {}}", command);

        var aggregate = eventSourcingHandler.getById(command.getId());
        aggregate.closeAccount();
        eventSourcingHandler.save(aggregate);

        log.info("Received CloseAccountCommand {command: {}}", command);
    }

    @Override
    public void handle(DepositFundsCommand command) {
        log.trace("Receiving DepositFundsCommand {command: {}}", command);

        var aggregate = eventSourcingHandler.getById(command.getId());
        aggregate.depositFunds(command.getAmount());
        eventSourcingHandler.save(aggregate);

        log.info("Received DepositFundsCommand {command: {}}", command);
    }

    @Override
    public void handle(WithdrawFundsCommand command) {
        log.trace("Receiving WithdrawFundsCommand {command: {}}", command);

        var aggregate = eventSourcingHandler.getById(command.getId());
        if (command.getAmount().compareTo(aggregate.getBalance()) > 0) {
            throw new IllegalStateException("Withdraw declined, insufficient funds");
        }
        aggregate.withdrawFunds(command.getAmount());
        eventSourcingHandler.save(aggregate);

        log.info("Received WithdrawFundsCommand {command: {}}", command);
    }

}
