package com.hhplus.io.app.event.application.scheduler;

import com.hhplus.io.app.event.domain.service.OutboxService;
import com.hhplus.io.app.event.infra.KafkaMessageProducer;
import com.hhplus.io.app.event.domain.entity.Outbox;
import com.hhplus.io.common.constants.Constants;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class OutboxScheduler {
    private final OutboxService outboxService;
    private final KafkaMessageProducer kafkaMessageProducer;

    public OutboxScheduler(OutboxService outboxService, KafkaMessageProducer kafkaMessageProducer) {
        this.outboxService = outboxService;
        this.kafkaMessageProducer = kafkaMessageProducer;
    }

    @Scheduled(fixedRate = 5000) // 5초마다 실행
    @Transactional
    public void processOutboxEvents() {
        List<Outbox> pendingEvents = outboxService.findPendingEvents();
        LocalDateTime now = LocalDateTime.now();
        for (Outbox event : pendingEvents) {
            //5분이 지나도록 발행되지 않은 outbox에 대한 처리
            if (event.getCreatedAt().isBefore(now.minusMinutes(5))) {
                try {
                    String topic = "";
                    switch (event.getDomainType()) {
                        case "TEMP_RESERVE" -> topic = Constants.KafkaTopics.TEMP_RESERVE_TOPIC;
                        case "PAYMENT" -> topic = Constants.KafkaTopics.PAYMENT_TOPIC;
                    }
                    event.published();
                    outboxService.save(event);
                    kafkaMessageProducer.send(topic, event.getEventKey(), event.getMessage());

                } catch (Exception e) {
                    log.error("Failed to process outbox event ID: {} , Error: {}", event.getOutboxId(), e.getMessage());
                }
            }

        }
    }
}
