package com.techbank.account.cmd.infrastructure;

import com.techbank.account.cmd.domain.AccountAggregate;
import com.techbank.account.cmd.domain.EventStoreRepository;
import com.techbank.cqrs.core.events.BaseEvent;
import com.techbank.cqrs.core.events.EventModel;
import com.techbank.cqrs.core.exceptions.AggregateNotFoundException;
import com.techbank.cqrs.core.exceptions.ConcurrencyException;
import com.techbank.cqrs.core.infrastructure.EventStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class AccountEventStore implements EventStore {

    private final EventStoreRepository eventStoreRepository;

    @Override
    public void saveEvents(String aggregateId, Iterable<BaseEvent> events, int expectedVersion) {
        var eventStoreList = eventStoreRepository.findByAggregateIdentifier(aggregateId);
        if (expectedVersion != -1 && eventStoreList.get(eventStoreList.size() - 1).getVersion() != expectedVersion) {
            throw new ConcurrencyException();
        }
        var version = expectedVersion;
        for (var event : events) {
            version++;
            event.setVersion(version);
            var eventModel = buildEventModel(aggregateId, event, version);
            var persistedEvent = eventStoreRepository.save(eventModel);
            if (nonNull(persistedEvent)) {
                // TODO: produce event to Kafka
            }
        }
    }

    @Override
    public List<BaseEvent> getEvents(String aggregateId) {
        var eventStoreList = eventStoreRepository.findByAggregateIdentifier(aggregateId);
        if (isNull(eventStoreList) || eventStoreList.isEmpty()) {
            throw new AggregateNotFoundException("Incorrect account ID provided");
        }
        return eventStoreList.stream()
                .map(EventModel::getEventData)
                .toList();
    }

    private EventModel buildEventModel(String aggregateId, BaseEvent event, int version) {
        return EventModel.builder()
                .timeStamp(new Date())
                .aggregateIdentifier(aggregateId)
                .aggregateType(AccountAggregate.class.getTypeName())
                .version(version)
                .eventType(event.getClass().getTypeName())
                .eventData(event)
                .build();
    }

}
