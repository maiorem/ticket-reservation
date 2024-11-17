package com.hhplus.io.usertoken.application;

import com.hhplus.io.app.usertoken.application.UserTokenInfo;
import com.hhplus.io.app.usertoken.application.UserTokenUseCase;
import com.hhplus.io.testcontainer.AcceptanceTest;
import com.hhplus.io.app.usertoken.domain.entity.WaitingQueueStatus;
import com.hhplus.io.app.usertoken.domain.entity.User;
import com.hhplus.io.app.usertoken.domain.entity.WaitingQueue;
import com.hhplus.io.app.usertoken.infra.UserJpaRepository;
import com.hhplus.io.app.usertoken.infra.UserTokenJpaRepository;
import com.hhplus.io.app.usertoken.infra.WaitingQueueJpaRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
class UserTokenUseCaseTest extends AcceptanceTest {

    @Autowired
    private UserTokenUseCase userTokenUseCase;
    @Autowired
    private UserTokenJpaRepository userTokenRepository;
    @Autowired
    private UserJpaRepository userRepository;
    @Autowired
    private WaitingQueueJpaRepository waitingQueueRepository;

    @BeforeEach
    void setUp() {
        User user1 = User.builder()
                .userId(1L).username("예이츠")
                .build();
        User user2 = User.builder()
                .userId(2L).username("워드워스")
                .build();
        User user3 = User.builder()
                .userId(3L).username("카프카")
                .build();
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
    }

    @Nested
    @DisplayName("유저 토큰 발급 시")
    class issueUserToken {

        @Test
        void 토큰_발급_성공() {
            //given
            Long userId = 1L;

            //when
            UserTokenInfo result = userTokenUseCase.issueUserToken(userId, null);

            //then
            assertNotNull(result);

        }

        @Test
        void 이미_해당_아이디로_대기가_존재하면_취소하고_다시_맨끝줄에_선다() {
            //given
            Long userId = 1L;
            String token = "aaaa";

            WaitingQueue queue1 = WaitingQueue.builder()
                    .queueId(2L)
                    .userId(userId)
                    .sequence(1L)
                    .status(String.valueOf(WaitingQueueStatus.WAITING))
                    .build();
            WaitingQueue queue2 = WaitingQueue.builder()
                    .queueId(3L)
                    .userId(3L)
                    .sequence(2L)
                    .status(String.valueOf(WaitingQueueStatus.WAITING))
                    .build();
            WaitingQueue queue3 = WaitingQueue.builder()
                    .queueId(4L)
                    .userId(4L)
                    .sequence(3L)
                    .status(String.valueOf(WaitingQueueStatus.WAITING))
                    .build();
            waitingQueueRepository.save(queue1);
            waitingQueueRepository.save(queue2);
            waitingQueueRepository.save(queue3);

            //when
            UserTokenInfo result = userTokenUseCase.issueUserToken(userId, token);

            //then
            assertEquals(3, result.sequence());
        }

    }

    @Nested
    @DisplayName("사용자 대기 순서 조회")
    class getSequence {

        @Test
        void 사용자_대기_순서_조회에_성공한다(){
            //given
            Long userId = 1L;
            String token = "aaaa";
            userTokenUseCase.issueUserToken(userId, token);
            userTokenUseCase.issueUserToken(userId, token);
            userTokenUseCase.issueUserToken(userId, token);

            //when
            Long sequence = userTokenUseCase.getSequence(token);

            //then
            assertEquals(3, sequence);
        }

    }

}