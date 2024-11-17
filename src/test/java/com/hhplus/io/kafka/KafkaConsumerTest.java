package com.hhplus.io.kafka;

import groovy.util.logging.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j

@Component
public class KafkaConsumerTest {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumerTest.class);
    private final List<String> messageList = new ArrayList<>();

    @KafkaListener(topics = "topic_test", groupId = "group_test")
    public void consume(String message) {
        log.info("Consumer Received message : {}", message);
        messageList.add(message);
    }

    public List<String> getMessageList() {
        return messageList;
    }
}
