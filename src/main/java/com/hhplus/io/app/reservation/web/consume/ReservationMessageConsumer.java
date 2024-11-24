package com.hhplus.io.app.reservation.web.consume;

import com.hhplus.io.app.dataPlatform.domain.event.DataPlatformEventListener;
import com.hhplus.io.app.event.domain.entity.Outbox;
import com.hhplus.io.app.event.domain.service.OutboxService;
import com.hhplus.io.app.reservation.domain.event.ReservationEvent;
import com.hhplus.io.app.usertoken.domain.event.TokenEventListener;
import com.hhplus.io.common.constants.Constants;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ReservationMessageConsumer {

    private final OutboxService outboxService;
    private final TokenEventListener tokenEventListener;
    private final DataPlatformEventListener dataPlatformEventListener;

    public ReservationMessageConsumer(OutboxService outboxService, TokenEventListener tokenEventListener, DataPlatformEventListener dataPlatformEventListener) {
        this.outboxService = outboxService;
        this.tokenEventListener = tokenEventListener;
        this.dataPlatformEventListener = dataPlatformEventListener;
    }

    @KafkaListener(topics = Constants.KafkaTopics.PAYMENT_TOPIC, groupId = "payment-outbox")
    public void outboxPulished(ConsumerRecord<String, String> consumerRecord) {
        Outbox outbox = outboxService.findByKey(consumerRecord.key());
        outbox.published();
        outboxService.save(outbox);
    }

    @KafkaListener(topics = Constants.KafkaTopics.PAYMENT_TOPIC, groupId = "payment-data")
    public void sendDataPlatform(ConsumerRecord<String, String> consumerRecord) {
        dataPlatformEventListener.reservationDataStoreEvent(ReservationEvent.convertFromString(consumerRecord.value()));
    }

    @KafkaListener(topics = Constants.KafkaTopics.PAYMENT_TOPIC, groupId = "payment-token")
    public void tokenExpired(ConsumerRecord<String, String> consumerRecord) {
        tokenEventListener.tokenExpired(ReservationEvent.convertFromString(consumerRecord.value()));
    }

}
