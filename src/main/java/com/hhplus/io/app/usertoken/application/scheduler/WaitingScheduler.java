package com.hhplus.io.app.usertoken.application.scheduler;

import com.hhplus.io.app.usertoken.application.UserTokenUseCase;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class WaitingScheduler {

    private final UserTokenUseCase userTokenUseCase;

    public WaitingScheduler(UserTokenUseCase userTokenUseCase) {
        this.userTokenUseCase = userTokenUseCase;
    }

    @Scheduled(cron = "*/10 * * * * *")
    @Transactional
    public void updateWaitingQueue(){
        userTokenUseCase.updateActive();
    }

}
