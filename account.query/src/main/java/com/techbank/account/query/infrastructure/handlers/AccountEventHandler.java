package com.techbank.account.query.infrastructure.handlers;

import com.techbank.account.common.events.AccountClosedEvent;
import com.techbank.account.common.events.AccountOpenedEvent;
import com.techbank.account.common.events.FundsDepositedEvent;
import com.techbank.account.common.events.FundsWithdrawnEvent;
import com.techbank.account.query.domain.AccountRepository;
import com.techbank.account.query.domain.BankAccount;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountEventHandler implements EventHandler {

    private final AccountRepository accountRepository;

    @Override
    public void on(AccountOpenedEvent event) {
        log.trace("Receiving AccountOpenedEvent {event: {}}", event);

        var bankAccount = buildBankAccount(event);
        var result = accountRepository.save(bankAccount);

        log.info("Received AccountOpenedEvent {event: {}, result: {}}", event, result);
    }

    @Override
    public void on(AccountClosedEvent event) {
        log.trace("Receiving AccountClosedEvent {event: {}}", event);

        accountRepository.deleteById(event.getId());

        log.info("Received AccountClosedEvent {event: {}}", event);
    }

    @Override
    public void on(FundsDepositedEvent event) {
        log.trace("Receiving FundsDepositedEvent {event: {}}", event);

        var result = accountRepository.findById(event.getId())
                .map(bankAccount -> bankAccount.depositFunds(event.getAmount()))
                .map(accountRepository::save)
                .orElse(null);

        log.info("Received FundsDepositedEvent {event: {}, result: {}}", event, result);
    }

    @Override
    public void on(FundsWithdrawnEvent event) {
        log.trace("Receiving FundsWithdrawnEvent {event: {}}", event);

        var result = accountRepository.findById(event.getId())
                .map(bankAccount -> bankAccount.withdrawFunds(event.getAmount()))
                .map(accountRepository::save)
                .orElse(null);

        log.info("Received FundsWithdrawnEvent {event: {}, result: {}}", event, result);
    }

    private BankAccount buildBankAccount(AccountOpenedEvent event) {
        return BankAccount.builder()
                .id(event.getId())
                .accountHolder(event.getAccountHolder())
                .creationDate(event.getCreatedDate())
                .accountType(event.getAccountType())
                .balance(event.getOpeningBalance())
                .build();
    }

}
