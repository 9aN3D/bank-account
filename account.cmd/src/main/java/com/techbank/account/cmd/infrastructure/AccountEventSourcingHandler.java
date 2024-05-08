package com.techbank.account.cmd.infrastructure;

import com.techbank.account.cmd.domain.AccountAggregate;
import com.techbank.cqrs.core.domain.AggregateRoot;
import com.techbank.cqrs.core.events.BaseEvent;
import com.techbank.cqrs.core.handlers.EventSourcingHandler;
import com.techbank.cqrs.core.infrastructure.EventStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;

import static java.util.Objects.nonNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountEventSourcingHandler implements EventSourcingHandler<AccountAggregate> {

    private final EventStore eventStore;

    @Override
    public void save(AggregateRoot aggregate) {
        log.trace("Saving aggregate {}", aggregate);

        eventStore.saveEvents(aggregate.getId(), aggregate.getUncommittedChanges(), aggregate.getVersion());
        aggregate.markChangesAsCommitted();

        log.info("Saved aggregate {}", aggregate);
    }

    @Override
    public AccountAggregate getById(String id) {
        log.trace("Getting aggregate by id {}", id);

        var aggregate = new AccountAggregate();
        var events = eventStore.getEvents(id);
        if (nonNull(events) && !events.isEmpty()) {
            aggregate.replayEvent(events);
            var latestVersion = events.stream()
                    .map(BaseEvent::getVersion)
                    .max(Comparator.naturalOrder());

            aggregate.setVersion(latestVersion.get());
        }

        log.info("Got aggregate by id {}, result {}", id, aggregate);
        return aggregate;
    }

}
