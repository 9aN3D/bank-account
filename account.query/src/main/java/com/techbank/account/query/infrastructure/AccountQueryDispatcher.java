package com.techbank.account.query.infrastructure;

import com.techbank.cqrs.core.domain.BaseEntity;
import com.techbank.cqrs.core.queries.BaseQuery;
import com.techbank.cqrs.core.queries.QueryDispatcher;
import com.techbank.cqrs.core.queries.QueryHandlerMethod;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Optional.ofNullable;

@Service
public class AccountQueryDispatcher implements QueryDispatcher {

    private final Map<Class<? extends BaseQuery>, QueryHandlerMethod> routes = new HashMap<>();

    @Override
    public <T extends BaseQuery> void registerHandler(Class<T> type, QueryHandlerMethod<T> query) {
        routes.put(type, query);
    }

    @Override
    public <E extends BaseEntity> List<E> send(BaseQuery query) {
        var handler = ofNullable(routes.get(query.getClass()))
                .orElseThrow(() -> new RuntimeException("No handler registered for query " + query.getClass()));
        return handler.handle(query);
    }

}
