package com.hhplus.io.user.application.usercase;

import com.hhplus.io.user.application.UserService;
import com.hhplus.io.user.application.UserTokenService;
import com.hhplus.io.user.application.WaitingQueueService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class UserTokenUseCaseTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserTokenService userTokenService;
    @Autowired
    private WaitingQueueService waitingQueueService;
    @Autowired
    private UserTokenUseCase userTokenUseCase;

    @Test
    public void 유저_토큰_발급(){

    }

    @Test
    public void 토큰으로_대기_순서_조회() {

    }


}