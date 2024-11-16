package com.hhplus.io.app.usertoken.domain.event;

import com.hhplus.io.app.usertoken.domain.service.WaitingQueueService;
import org.springframework.stereotype.Component;

@Component
public class TokenEventListener {

    private final WaitingQueueService waitingQueueService;

    public TokenEventListener(WaitingQueueService waitingQueueService) {
        this.waitingQueueService = waitingQueueService;
    }

    public void tokenExpired(String token) {
        waitingQueueService.expireToken(token);
    }
}
