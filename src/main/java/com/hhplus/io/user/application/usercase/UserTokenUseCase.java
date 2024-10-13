package com.hhplus.io.user.application.usercase;

import com.hhplus.io.user.application.UserService;
import com.hhplus.io.user.application.UserTokenService;
import com.hhplus.io.user.application.WaitingQueueService;
import org.springframework.stereotype.Service;

@Service
public class UserTokenUseCase {

    private final UserService userService;
    private final UserTokenService userTokenService;
    private final WaitingQueueService waitingQueueService;

    public UserTokenUseCase(UserService userService, UserTokenService userTokenService, WaitingQueueService waitingQueueService) {
        this.userService = userService;
        this.userTokenService = userTokenService;
        this.waitingQueueService = waitingQueueService;
    }




}
