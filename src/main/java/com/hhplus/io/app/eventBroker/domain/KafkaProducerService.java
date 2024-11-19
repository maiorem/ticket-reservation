package com.hhplus.io.app.eventBroker.domain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(String topic, String key, String payload) {
        kafkaTemplate.send(topic, key, payload).whenComplete((result, exception) -> {
            if (exception == null) {
                // 성공 처리
                log.info("Message sent to Kafka:Topic:{}, Partition:{}, Offset:{}"
                        ,result.getRecordMetadata().topic()
                        ,result.getRecordMetadata().partition()
                        ,result.getRecordMetadata().offset());
            } else {
                // 실패 처리
                log.error("Failed to send message to Kafka: {} ", exception.getMessage());
                throw new RuntimeException("Failed to send message to Kafka", exception);
            }
        });
    }
}
