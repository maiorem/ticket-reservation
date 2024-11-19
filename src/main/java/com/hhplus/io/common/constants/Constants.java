package com.hhplus.io.common.constants;

public class Constants {

    public static class CacheText {
        public static final String SEPARATOR = "!@";
        public static final String RESERVE_SEAT_KEY_PREFIX = "reserveSeat:";
        public static final String WAITING_QUEUE_KEY = "waitingQueue";
        public static final String ACTIVE_QUEUE_KEY_PREFIX = "activeQueue:";
    }

    public static class KafkaTopics {
        public static final String TEMP_RESERVE_TOPIC = "temp-reserve-topic";
        public static final String PAYMENT_TOPIC = "payment-topic";
    }

}
