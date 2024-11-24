package com.hhplus.io.app.reservation.domain.event;

import com.hhplus.io.app.event.domain.entity.DomainType;
import com.hhplus.io.app.event.domain.entity.EventType;
import com.hhplus.io.app.event.domain.entity.Outbox;
import com.hhplus.io.app.event.domain.service.OutboxService;
import com.hhplus.io.app.event.infra.KafkaMessageProducer;
import com.hhplus.io.common.constants.Constants;
import com.hhplus.io.common.support.utils.JsonUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class ReservationListner {

    private final OutboxService outboxService;
    private final KafkaMessageProducer kafkaMessageProducer;

    public ReservationListner(OutboxService outboxService, KafkaMessageProducer kafkaMessageProducer) {
        this.outboxService = outboxService;
        this.kafkaMessageProducer = kafkaMessageProducer;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void save(ReservationEvent event) {
        outboxService.save(Outbox.create(DomainType.PAYMENT, EventType.PAYMENT_SUCCESS, event.key(), JsonUtils.toJsonString(event)));
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void reservationProduce(ReservationEvent event) {
        kafkaMessageProducer.send(Constants.KafkaTopics.PAYMENT_TOPIC, event.key(), JsonUtils.toJsonString(event));
    }

}
