package com.techbank.account.query.api.queries;

import com.techbank.account.query.api.dto.EqualityType;
import com.techbank.account.query.api.exceptions.AccountNotFoundException;
import com.techbank.account.query.domain.AccountRepository;
import com.techbank.cqrs.core.domain.BaseEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountQueryHandler implements QueryHandler {

    private final AccountRepository accountRepository;

    @Override
    public List<BaseEntity> handle(FindAccountByHolderQuery query) {
        log.trace("Getting by FindAccountByHolderQuery {query: {}}", query);

        var result = accountRepository.findByAccountHolder(query.getAccountHolder())
                .map(List::of)
                .orElseThrow(() -> new AccountNotFoundException(query));

        log.info("Got by FindAccountByHolderQuery {query: {}, resultSize: {}}", query, result.size());
        return new ArrayList<>(result);
    }

    @Override
    public List<BaseEntity> handle(FindAccountByIdQuery query) {
        log.trace("Getting by FindAccountByIdQuery {query: {}}", query);

        var result = accountRepository.findById(query.getId())
                .map(List::of)
                .orElseThrow(() -> new AccountNotFoundException(query));

        log.info("Got by FindAccountByIdQuery {query: {}, resultSize: {}}", query, result.size());
        return new ArrayList<>(result);
    }

    @Override
    public List<BaseEntity> handle(FindAccountWithBalanceQuery query) {
        log.trace("Getting by FindAccountWithBalanceQuery {query: {}}", query);

        var result = query.getEqualityType() == EqualityType.GREATER_THAN
                ? accountRepository.findByBalanceGreaterThan(query.getBalance())
                : accountRepository.findByBalanceLessThan(query.getBalance());

        log.info("Got by FindAccountWithBalanceQuery {query: {}, resultSize: {}}", query, result.size());
        return result;
    }

    @Override
    public List<BaseEntity> handle(FindAllAccountsQuery query) {
        log.trace("Getting by FindAllAccountsQuery");

        var bankAccounts = accountRepository.findAll();
        var result = new ArrayList<BaseEntity>();
        bankAccounts.forEach(result::add);

        log.info("Got by FindAllAccountsQuery {query: {}, resultSize: {}}", query, result.size());
        return result;
    }

}
