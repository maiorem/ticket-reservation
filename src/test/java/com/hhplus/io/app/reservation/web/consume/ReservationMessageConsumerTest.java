package com.hhplus.io.app.reservation.web.consume;

import com.hhplus.io.app.concert.application.SeatUseCaseDTO;
import com.hhplus.io.app.concert.domain.entity.SeatStatus;
import com.hhplus.io.app.event.domain.entity.DomainType;
import com.hhplus.io.app.event.domain.entity.EventType;
import com.hhplus.io.app.event.domain.entity.Outbox;
import com.hhplus.io.app.event.infra.KafkaMessageProducer;
import com.hhplus.io.app.event.infra.OutboxJpaRepository;
import com.hhplus.io.app.reservation.domain.dto.ConfirmReservationInfo;
import com.hhplus.io.app.reservation.domain.event.ReservationEvent;
import com.hhplus.io.app.usertoken.domain.repository.WaitingQueueRepository;
import com.hhplus.io.common.constants.Constants;
import com.hhplus.io.common.support.utils.JsonUtils;
import com.hhplus.io.testcontainer.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

class ReservationMessageConsumerTest extends AcceptanceTest {

    @Autowired
    WaitingQueueRepository waitingQueueRepository;

    @Autowired
    OutboxJpaRepository outboxRepository;

    @Autowired
    KafkaMessageProducer kafkaMessageProducer;

    @Test
    @DisplayName("결제 성공 메시지 발행 후 토큰 만료 확인")
    void 메시지_발행_성공() {
        //given
        String token = "aaaa";
        waitingQueueRepository.createActiveQueue(token);
        Boolean isActive = waitingQueueRepository.isActive(token);
        assertTrue(isActive);

        ConfirmReservationInfo info = ConfirmReservationInfo.of(
                "홍세영",
                "조수미 콘서트",
                LocalDateTime.now(),
                LocalDateTime.now(),
                1,
                10000,
                List.of(SeatUseCaseDTO.of(1L, "01", SeatStatus.CONFIRMED, 10000))
        );
        ReservationEvent event = ReservationEvent.create(info, token);
        Outbox outbox = Outbox.create(DomainType.PAYMENT, EventType.PAYMENT_SUCCESS, event.key(), JsonUtils.toJsonString(event));
        outboxRepository.save(outbox);

        //when
        kafkaMessageProducer.send(Constants.KafkaTopics.PAYMENT_TOPIC, event.key(), JsonUtils.toJsonString(event));

        //then
        await().pollDelay(2, TimeUnit.SECONDS)
                .atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> assertTrue(outboxRepository.findByEventKey(event.key()).orElseThrow().isPublished()));
        
        Boolean result = waitingQueueRepository.isActive(token);
        assertFalse(result);       

    }
}