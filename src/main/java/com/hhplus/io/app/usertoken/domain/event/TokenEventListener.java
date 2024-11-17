package com.hhplus.io.app.usertoken.domain.event;

import com.hhplus.io.app.reservation.domain.event.ReservationEvent;
import com.hhplus.io.app.usertoken.domain.service.WaitingQueueService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class TokenEventListener {

    private final WaitingQueueService waitingQueueService;

    public TokenEventListener(WaitingQueueService waitingQueueService) {
        this.waitingQueueService = waitingQueueService;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void tokenExpired(ReservationEvent event) {
        waitingQueueService.expireToken(event.token());
    }
}
