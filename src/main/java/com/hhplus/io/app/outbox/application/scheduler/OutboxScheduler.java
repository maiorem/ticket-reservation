package com.hhplus.io.app.outbox.application.scheduler;

import com.hhplus.io.app.eventBroker.domain.KafkaProducerService;
import com.hhplus.io.app.outbox.domain.entity.Outbox;
import com.hhplus.io.app.outbox.domain.repository.OutboxRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class OutboxScheduler {
    private final OutboxRepository outboxRepository;
    private final KafkaProducerService kafkaProducerService;

    @Value("${kafka.topic.default}")
    private String kafkaTopic;

    public OutboxScheduler(OutboxRepository outboxRepository, KafkaProducerService kafkaProducerService) {
        this.outboxRepository = outboxRepository;
        this.kafkaProducerService = kafkaProducerService;
    }

    @Scheduled(fixedRate = 5000) // 5초마다 실행
    @Transactional
    public void processOutboxEvents() {
        List<Outbox> pendingEvents = outboxRepository.findPendingEvents();
        for (Outbox event : pendingEvents) {
            try {
                kafkaProducerService.sendEvent(kafkaTopic, event.getAggregateId(), event.getMessage());
                event.setStatus(true);
                outboxRepository.save(event);

            } catch (Exception e) {
                log.error("Failed to process outbox event ID: " + event.getOutboxId() + " Error: " + e.getMessage());
                // 실패처리

            }
        }
    }
}
