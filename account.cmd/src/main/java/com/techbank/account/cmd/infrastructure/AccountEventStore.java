package com.techbank.account.cmd.infrastructure;

import com.techbank.account.cmd.domain.AccountAggregate;
import com.techbank.account.cmd.domain.EventStoreRepository;
import com.techbank.cqrs.core.events.BaseEvent;
import com.techbank.cqrs.core.events.EventModel;
import com.techbank.cqrs.core.exceptions.AggregateNotFoundException;
import com.techbank.cqrs.core.exceptions.ConcurrencyException;
import com.techbank.cqrs.core.infrastructure.EventStore;
import com.techbank.cqrs.core.producers.EventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static java.util.Objects.isNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountEventStore implements EventStore {

    private final EventStoreRepository eventStoreRepository;
    private final EventProducer eventProducer;

    @Override
    public void saveEvents(String aggregateId, Iterable<BaseEvent> events, int expectedVersion) {
        log.info("Saving events for {aggregateId: {}, eventsSize: {}, expectedVersion: {}}", aggregateId, events.spliterator().getExactSizeIfKnown(), expectedVersion);

        var eventStoreList = eventStoreRepository.findByAggregateIdentifier(aggregateId);
        if (expectedVersion != -1 && eventStoreList.get(eventStoreList.size() - 1).getVersion() != expectedVersion) {
            throw new ConcurrencyException();
        }
        var version = expectedVersion;
        var persistedEventCount = 0;
        for (var event : events) {
            version++;
            event.setVersion(version);
            var eventModel = buildEventModel(aggregateId, event, version);
            var persistedEvent = eventStoreRepository.save(eventModel);
            if (!persistedEvent.getId().isEmpty()) {
                eventProducer.produce(event.getClass().getSimpleName(), event);
                persistedEventCount++;
            }
        }

        log.info("Saved events for {aggregateId: {}, resultSize: {}, expectedVersion: {}", aggregateId, persistedEventCount, expectedVersion);
    }

    @Override
    public List<BaseEvent> getEvents(String aggregateId) {
        log.trace("Getting events for {aggregateId: {}}", aggregateId);

        var eventStoreList = eventStoreRepository.findByAggregateIdentifier(aggregateId);
        if (isNull(eventStoreList) || eventStoreList.isEmpty()) {
            throw new AggregateNotFoundException(String.format("Incorrect account ID provided, %s", aggregateId));
        }
        var result = eventStoreList.stream()
                .map(EventModel::getEventData)
                .toList();

        log.info("Got events for {aggregateId: {}, resultSize: {}}", aggregateId, result.size());
        return result;
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
