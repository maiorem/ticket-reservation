package com.hhplus.io.usertoken.web.scheduler;

import com.hhplus.io.usertoken.application.UserTokenUseCase;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class WaitingScheduler {

    private final UserTokenUseCase userTokenUseCase;

    public WaitingScheduler(UserTokenUseCase userTokenUseCase) {
        this.userTokenUseCase = userTokenUseCase;
    }

    @Scheduled(fixedDelay = 5000)
    public void updateWaitingQueue(){
        userTokenUseCase.updateWaitingQueue();
    }

    @Scheduled(fixedDelay = 5000)
    public void deleteExpiryWaitingQueue(){
        userTokenUseCase.deleteExpiryWaitingQueue();
    }
}
