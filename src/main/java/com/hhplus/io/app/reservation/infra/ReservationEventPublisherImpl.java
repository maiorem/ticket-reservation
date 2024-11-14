package com.hhplus.io.app.reservation.infra;

import com.hhplus.io.app.reservation.domain.event.ReservationEvent;
import com.hhplus.io.app.reservation.domain.event.ReservationEventPublisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class ReservationEventPublisherImpl implements ReservationEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public ReservationEventPublisherImpl(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void publish(ReservationEvent reservationEvent) {
        applicationEventPublisher.publishEvent(reservationEvent);
    }
}
