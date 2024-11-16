package com.hhplus.io.usertoken.domain;

import com.hhplus.io.app.usertoken.domain.service.UserService;
import com.hhplus.io.app.usertoken.domain.service.WaitingQueueService;
import com.hhplus.io.app.usertoken.domain.entity.User;
import com.hhplus.io.app.usertoken.domain.entity.WaitingQueue;
import com.hhplus.io.app.usertoken.domain.entity.WaitingQueueStatus;
import com.hhplus.io.app.usertoken.domain.repository.UserRepository;
import com.hhplus.io.app.usertoken.domain.repository.WaitingQueueRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WaitingQueueServiceTest {

    @Mock
    private WaitingQueueRepository waitingQueueRepository;

    @InjectMocks
    private WaitingQueueService waitingQueueInfo;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        waitingQueueRepository = mock(WaitingQueueRepository.class);
        waitingQueueInfo = new WaitingQueueService(waitingQueueRepository);
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    @DisplayName("사용자와 대기열 상태로 현재 대기 조회")
    void getQueueByUserAndStatus() {
        //given
        Long userId =1L;
        Long queueId = 1L;
        WaitingQueue queue = WaitingQueue.builder().userId(userId).queueId(queueId).status(String.valueOf(WaitingQueueStatus.WAITING)).sequence(2L).build();
        when(waitingQueueRepository.getQueueByUserAndStatus(userId, WaitingQueueStatus.WAITING)).thenReturn(queue);

        //when
        WaitingQueue result = waitingQueueInfo.getQueueByUserAndStatus(userId, WaitingQueueStatus.WAITING);

        //then
        assertEquals(2L, result.getSequence());

    }

    @Test
    @DisplayName("대기열 상태 업데이트")
    void updateQueueStatus() {
        //given
        Long queueId = 1L;
        WaitingQueue queue = WaitingQueue.builder().userId(1L).queueId(queueId).status(String.valueOf(WaitingQueueStatus.WAITING)).sequence(2L).build();

        //when
        waitingQueueInfo.updateQueueStatus(queue, WaitingQueueStatus.PROCESS);

        //then
        assertEquals(String.valueOf(WaitingQueueStatus.PROCESS), queue.getStatus());
    }

    @Test
    @DisplayName("대기열 생성 및 현재 순서 조회(맨 끝줄)")
    void createQueue() {
        //given
        Long userId = 3L;
        String token = "aaaa";
        User user1 = User.builder().userId(1L).username("홍길동").build();
        User user2 = User.builder().userId(2L).username("임꺽정").build();
        User user3 = User.builder().userId(userId).username("홍세영").build();
        WaitingQueue testQueue1 = WaitingQueue.builder().userId(1L).queueId(1L).sequence(1L).build();
        WaitingQueue testQueue2 = WaitingQueue.builder().userId(2L).queueId(2L).sequence(2L).build();
        when(userRepository.getUser(1L)).thenReturn(user1);
        when(userRepository.getUser(2L)).thenReturn(user2);
        when(userRepository.getUser(userId)).thenReturn(user3);
        when(waitingQueueRepository.getQueueByUserAndStatus(1L, WaitingQueueStatus.WAITING)).thenReturn(testQueue1);
        when(waitingQueueRepository.getQueueByUserAndStatus(2L, WaitingQueueStatus.WAITING)).thenReturn(testQueue2);

        //when
        String queue = waitingQueueInfo.createQueue(token);

        //then
        assertNotNull(queue);
    }

    @Test
    @DisplayName("현재 대기중인 대기열 전체 수 조회")
    void countByQueueStatus() {
        //given
        WaitingQueue testQueue1 = WaitingQueue.builder().userId(1L).queueId(1L).sequence(1L).build();
        WaitingQueue testQueue2 = WaitingQueue.builder().userId(2L).queueId(2L).sequence(2L).build();
        when(waitingQueueRepository.getQueueByUserAndStatus(1L, WaitingQueueStatus.WAITING)).thenReturn(testQueue1);
        when(waitingQueueRepository.getQueueByUserAndStatus(2L, WaitingQueueStatus.WAITING)).thenReturn(testQueue2);

        //when
        Long count = waitingQueueInfo.countByQueueStatus(WaitingQueueStatus.WAITING);

        //then
        assertEquals(2L, count);
    }

}