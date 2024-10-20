package com.hhplus.io.usertoken.application;

import com.hhplus.io.common.exception.TokenExpireException;
import com.hhplus.io.common.exception.TokenNotVaildationException;
import com.hhplus.io.common.response.ErrorCode;
import com.hhplus.io.usertoken.domain.WaitingQueueStatus;
import com.hhplus.io.usertoken.domain.User;
import com.hhplus.io.usertoken.domain.UserToken;
import com.hhplus.io.usertoken.domain.WaitingQueue;
import com.hhplus.io.usertoken.domain.UserRepository;
import com.hhplus.io.usertoken.domain.UserTokenRepository;
import com.hhplus.io.usertoken.domain.WaitingQueueRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserTokenUseCaseTest {

    @Autowired
    private UserTokenUseCase userTokenUseCase;
    @Autowired
    private UserTokenRepository userTokenRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WaitingQueueRepository waitingQueueRepository;

    @Nested
    @DisplayName("유저 토큰 발급 시")
    class issueUserToken {

        @Test
        void 토큰_발급_성공() {
            //given
            Long userId = 1L;
            User user = User.builder()
                    .userId(userId).username("홍세영")
                    .build();
            userRepository.saveUser(user);

            //when
            UserTokenCommand result = userTokenUseCase.issueUserToken(userId);

            //then
            UserToken token = userTokenRepository.getToken(userId);

            assertEquals(1, result.sequence());
            assertEquals(token.getToken(), result.token());
            assertEquals(token.getTokenExpire(), result.expiresAt());
        }

        @Test
        void 이미_해당_아이디로_대기가_존재하면_취소하고_다시_맨끝줄에_선다() {
            //given
            Long userId = 1L;
            User user1 = User.builder()
                    .userId(userId).username("홍길동")
                    .build();
            User user2 = User.builder()
                    .userId(2L).username("홍길동")
                    .build();
            User user3 = User.builder()
                    .userId(3L).username("홍길동")
                    .build();

            userRepository.saveUser(user1);
            userRepository.saveUser(user2);
            userRepository.saveUser(user3);

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
            waitingQueueRepository.generateQueue(queue1);
            waitingQueueRepository.generateQueue(queue2);
            waitingQueueRepository.generateQueue(queue3);

            //when
            UserTokenCommand result = userTokenUseCase.issueUserToken(userId);

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
            User user1 = User.builder()
                    .userId(userId).username("홍길동")
                    .build();
            User user2 = User.builder()
                    .userId(2L).username("홍길동")
                    .build();
            User user3 = User.builder()
                    .userId(3L).username("홍길동")
                    .build();
            userRepository.saveUser(user1);
            userRepository.saveUser(user2);
            userRepository.saveUser(user3);

            userTokenUseCase.issueUserToken(2L);
            userTokenUseCase.issueUserToken(3L);
            userTokenUseCase.issueUserToken(userId);

            //when
            UserToken token = userTokenRepository.getToken(userId);
            Long sequence = userTokenUseCase.getSequence(userId, token.getToken());

            //then
            assertEquals(3, sequence);
        }

        @Test
        void 사용자와_토큰정보가_일치하지_않으면_exception을_반환한다() {
            //given
            Long testUserId = 1L;
            User user1 = User.builder()
                    .userId(testUserId).username("홍길동")
                    .build();
            User user2 = User.builder()
                    .userId(2L).username("홍길동")
                    .build();
            userRepository.saveUser(user1);
            userRepository.saveUser(user2);
            UserToken dummyToken = UserToken.builder().userId(1L).token("2222bbbbb").isActive(true)
                    .tokenExpire(LocalDateTime.now().plusHours(1)).build();
            UserToken testToken = UserToken.builder().userId(2L).token("1111aaaa").isActive(true)
                    .tokenExpire(LocalDateTime.now().plusHours(1)).build();
            userTokenRepository.generateToken(dummyToken);
            userTokenRepository.generateToken(testToken);

            //when
            TokenNotVaildationException exception = assertThrows(TokenNotVaildationException.class, () -> userTokenUseCase.getSequence(testUserId, testToken.getToken()));

            //then
            assertEquals(ErrorCode.TOKEN_INVALID.getStatusCode(), exception.getStatusCode());
        }

        @Test
        void 이미_만료된_토큰이면_exception을_반환한다() {
            //given
            Long userId = 1L;
            User user1 = User.builder()
                    .userId(userId).username("홍길동")
                    .build();
            userRepository.saveUser(user1);
            UserToken token = UserToken.builder().userId(1L).token("1111aaaa").isActive(true)
                    .tokenExpire(LocalDateTime.now().minusHours(1)).build();
            userTokenRepository.generateToken(token);

            //when
            TokenExpireException exception = assertThrows(TokenExpireException.class, () -> userTokenUseCase.getSequence(userId, token.getToken()));

            //then
            assertEquals(ErrorCode.TOKEN_EXPIRED.getStatusCode(), exception.getStatusCode());
        }


    }
}