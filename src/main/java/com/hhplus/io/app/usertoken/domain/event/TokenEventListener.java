package com.hhplus.io.app.usertoken.domain.event;

import com.hhplus.io.app.reservation.domain.event.ReservationEvent;
import com.hhplus.io.app.usertoken.domain.service.WaitingQueueService;
import org.springframework.stereotype.Component;

@Component
public class TokenEventListener {

    private final WaitingQueueService waitingQueueService;

    public TokenEventListener(WaitingQueueService waitingQueueService) {
        this.waitingQueueService = waitingQueueService;
    }

    public void tokenExpired(ReservationEvent event) {
        waitingQueueService.expireToken(event.token());
    }
}
