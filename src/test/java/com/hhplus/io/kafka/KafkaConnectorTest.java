package com.hhplus.io.kafka;

import com.hhplus.io.testcontainer.AcceptanceTest;
import groovy.util.logging.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@Slf4j
public class KafkaConnectorTest extends AcceptanceTest {

    @Autowired
    private KafkaProducerTest kafkaProducerTest;
    @Autowired
    private KafkaConsumerTest kafkaConsumerTest;

    @Test
    @DisplayName("Kafka 연동 테스트")
    void kafkaTest() {

        int cnt = 10;
        String topic = "topic_test";
        for (int i = 0; i < cnt; i++) {
            kafkaProducerTest.send(topic, "Producer Sending Message : " + (i + 1));
        }

        await().pollDelay(2, TimeUnit.SECONDS)
                .atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> assertThat(kafkaConsumerTest.getMessageList())
                        .hasSize(cnt));
    }
}
