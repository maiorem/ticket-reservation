package com.hhplus.io.app.reservation.domain.event;

import org.springframework.stereotype.Component;

@Component
public interface ReservationEventPublisher {
    void publish(ReservationEvent reservationEvent);
}
