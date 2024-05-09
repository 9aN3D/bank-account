package com.techbank.account.query.api.queries;

import com.techbank.cqrs.core.domain.BaseEntity;

import java.util.List;

public interface QueryHandler {

    List<BaseEntity> hande(FindAccountByHolderQuery query);

    List<BaseEntity> hande(FindAccountByIdQuery query);

    List<BaseEntity> hande(FindAccountWithBalanceQuery query);

    List<BaseEntity> hande(FindAllAccountsQuery query);

}
