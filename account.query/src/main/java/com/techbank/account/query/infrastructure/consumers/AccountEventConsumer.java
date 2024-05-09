package com.techbank.account.query.infrastructure.consumers;

import com.techbank.account.common.events.AccountClosedEvent;
import com.techbank.account.common.events.AccountOpenedEvent;
import com.techbank.account.common.events.FundsDepositedEvent;
import com.techbank.account.common.events.FundsWithdrawnEvent;
import com.techbank.cqrs.core.events.BaseEvent;
import com.techbank.cqrs.core.infrastructure.EventDispatcher;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountEventConsumer implements EventConsumer {

    private final EventDispatcher eventDispatcher;

    @Override
    @KafkaListener(topics = "AccountOpenedEvent", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(AccountOpenedEvent event, Acknowledgment ack) {
        dispatchAndAcknowledge(event, ack);
    }

    @Override
    @KafkaListener(topics = "AccountClosedEvent", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(AccountClosedEvent event, Acknowledgment ack) {
        dispatchAndAcknowledge(event, ack);
    }

    @Override
    @KafkaListener(topics = "FundsDepositedEvent", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(FundsDepositedEvent event, Acknowledgment ack) {
        dispatchAndAcknowledge(event, ack);
    }

    @Override
    @KafkaListener(topics = "FundsWithdrawnEvent", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(FundsWithdrawnEvent event, Acknowledgment ack) {
        dispatchAndAcknowledge(event, ack);
    }

    private void dispatchAndAcknowledge(BaseEvent event, Acknowledgment ack) {
        eventDispatcher.on(event);
        ack.acknowledge();
    }

}
