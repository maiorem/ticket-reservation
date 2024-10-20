package com.hhplus.io.usertoken.application;

import com.hhplus.io.common.exception.TokenExpireException;
import com.hhplus.io.common.exception.TokenNotVaildationException;
import com.hhplus.io.usertoken.domain.WaitingQueueStatus;
import com.hhplus.io.usertoken.domain.User;
import com.hhplus.io.usertoken.domain.WaitingQueue;
import com.hhplus.io.usertoken.service.UserService;
import com.hhplus.io.usertoken.domain.UserToken;
import com.hhplus.io.usertoken.service.UserTokenService;
import com.hhplus.io.usertoken.service.WaitingQueueService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;



@Service
public class UserTokenUseCase {

    private final UserService userService;
    private final UserTokenService userTokenService;
    private final WaitingQueueService waitingQueueService;

    //서비스 수용 인원 관리
    private final long MAX_PROCESSING_VOLUME = 50;

    public UserTokenUseCase(UserService userService, UserTokenService userTokenService, WaitingQueueService waitingQueueService) {
        this.userService = userService;
        this.userTokenService = userTokenService;
        this.waitingQueueService = waitingQueueService;
    }

    /**
     * 유저 토큰 발급
     * - 현재 대기 중인 대기열이 존재하는지 검증
     * - 존재하면 대기열 초기화 후 제일 뒤로 업데이트
     * - 토큰 생성 후 반환
     */
    @Transactional
    public UserTokenCommand issueUserToken(Long userId) {
        //사용자 조회
        User user = userService.getUser(userId);

        //현재 대기상태 대기열 존재 여부
        WaitingQueue queue = waitingQueueService.getWaitingQueueByUser(userId, WaitingQueueStatus.WAITING);
        if (queue != null) {
            //존재하면
            waitingQueueService.updateStatus(queue, WaitingQueueStatus.CANCEL);
        }
        // 대기열 및 토큰 생성
        WaitingQueue waitingQueue = waitingQueueService.createQueue(user);
        UserToken userToken = userTokenService.createUserToken(user);

        return UserTokenCommand.of(user.getUserId(), waitingQueue.getSequence(), userToken.getToken(), userToken.getTokenExpire());
    }

    /**
     * 대기열 수동 업데이트 (순서가 밀리거나 이상 발생 시 실행)
     * - 서비스 수용인원 max 체크하여 현재 서비스 진행 중인(status = PROCESS) 대기열 갯수 유지
     * - 갯수가 변경되면 빠진 수만큼 대기 중인(status = WAITING) 대기열 PROCESS로 변경 후 순서 업데이트
     */
    @Transactional
    public void updateWaitingQueue(){
        Long count = waitingQueueService.countWaitingQueueByStatus(WaitingQueueStatus.PROCESS);
        long updateProcess = MAX_PROCESSING_VOLUME - count;
        if (updateProcess > 0) {
            waitingQueueService.updateAllWaitingQueue(updateProcess);
        }
    }

    /**
     * 사용자 대기열 순서 조회
     * - 사용자 현재 대기 순서 조회
     */
    public Long getSequence(Long userId, String token) {
        //사용자 조회
        User user = userService.getUser(userId);

        //사용자와 토큰 validation 체크 (fail시 exception)
        if (!userTokenService.validate(user, token)){
            throw new TokenNotVaildationException("토큰이 사용자와 일치하지 않습니다.");
        }

        //토큰 잔여시간 검사 (만료 시 exception)
        if(userTokenService.isExpireToken(token)) {
            throw new TokenExpireException("토큰 만료시간을 초과하였습니다.");
        }
        //사용자 현재 대기열 순서 조회
        return waitingQueueService.getSequence(userId);
    }




}
