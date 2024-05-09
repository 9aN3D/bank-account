package com.techbank.account.query.infrastructure.handlers;

import com.techbank.cqrs.core.events.BaseEvent;
import com.techbank.cqrs.core.events.EventHandlerMethod;
import com.techbank.cqrs.core.infrastructure.EventDispatcher;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static java.util.Optional.ofNullable;

@Service
public class AccountEventDispatcher implements EventDispatcher {

    private final Map<Class<? extends BaseEvent>, EventHandlerMethod> routes = new HashMap<>();

    @Override
    public <T extends BaseEvent> void registerHandler(Class<T> type, EventHandlerMethod<T> handlerMethod) {
        routes.put(type, handlerMethod);
    }

    @Override
    public void on(BaseEvent event) {
        var handler = ofNullable(routes.get(event.getClass()))
                .orElseThrow(() -> new RuntimeException("No handler registered for event " + event.getClass()));
        handler.on(event);
    }

}
