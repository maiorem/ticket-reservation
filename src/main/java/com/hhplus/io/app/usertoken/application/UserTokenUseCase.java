package com.hhplus.io.app.usertoken.application;

import com.hhplus.io.app.usertoken.domain.service.WaitingQueueService;
import com.hhplus.io.app.usertoken.domain.service.UserService;
import com.hhplus.io.common.support.domain.error.CoreException;
import com.hhplus.io.common.support.domain.error.ErrorType;
import com.hhplus.io.app.usertoken.domain.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserTokenUseCase {

    private final UserService userService;
    private final WaitingQueueService waitingQueueService;

    //서비스 수용 인원 관리 (1000명으로 임시 설정)
    private final long MAX_PROCESSING_VOLUME = 1000;

    public UserTokenUseCase(UserService userService, WaitingQueueService waitingQueueService) {
        this.userService = userService;
        this.waitingQueueService = waitingQueueService;
    }

    /**
     * 유저 토큰 발급
     * - 현재 대기 중인 대기열이 존재하는지 검증
     * - 존재하면 대기열 초기화 후 제일 뒤로 업데이트
     * - 토큰 생성 후 반환
     */
    @Transactional
    public UserTokenCommand issueUserToken(Long userId, String token) {
        //사용자 조회
        User user = userService.getUser(userId);

        //현재 대기상태 대기열 존재 여부
        if (token != null) {
            //대기열 토큰이 이미 존재하면 삭제
            waitingQueueService.initQueue(token);
        }
        // 대기열 및 토큰 생성
        String createdToken = waitingQueueService.createQueue(token);

        // 대기열 순서 조회
        Long sequence = waitingQueueService.getRank(token);

        return UserTokenCommand.of(user.getUserId(), sequence, createdToken);
    }

    /**
     * 사용자 대기열 순서 조회
     * - 사용자 현재 대기 순서 조회
     */
    public Long getSequence(String token) {
        //사용자 현재 대기열 순서 조회
        return waitingQueueService.getRank(token);
    }

    /**
     * 대기 -> 활성으로 상태 전환
     * - REDIS
     */
    public void updateActive() {
        List<String> tokenList = waitingQueueService.getWaitingQueueList(MAX_PROCESSING_VOLUME);
        waitingQueueService.activateAll(tokenList);
    }


    /**
     * 토큰 만료시간 연장
     * - REDIS
     */
    public void refreshToken(String token) {
        if(waitingQueueService.isActive(token)) {
            waitingQueueService.refreshToken(token);
        } else {
            throw new CoreException(ErrorType.EXPIRED_TOKEN);
        }
    }

    /**
     * 활성 큐 만료처리
     * - REDIS
     */
    public void expireToken(String token) {
        waitingQueueService.expireToken(token);
    }


//    /**
//     * 대기열 순서 업데이트 (순서가 밀리거나 이상 발생 시 실행)
//     * - 서비스 수용인원 max 체크하여 현재 서비스 진행 중인(status = PROCESS) 대기열 갯수 유지
//     * - 갯수가 변경되면 빠진 수만큼 대기 중인(status = WAITING) 대기열 PROCESS로 변경 후 순서 업데이트
//     * - RDB
//     */
//    @Transactional
//    public void updateWaitingQueue(){
//        Long count = waitingQueueService.countWaitingQueueByStatus(WaitingQueueStatus.PROCESS);
//        long updateProcess = MAX_PROCESSING_VOLUME - count;
//        if (updateProcess > 0) {
//            waitingQueueService.updateAllWaitingQueue(updateProcess);
//        }
//    }
//
//    /**
//     * 만료 대기열 삭제
//     * - 전체 대기열을 조회하여 일정 시간이 지나도 프로세스 진행이 되지 않는 대기열 자동 삭제
//     * - RDB
//     */
//    @Transactional
//    public void deleteExpiryWaitingQueue(){
//        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
//        //최대 대기 1시간
//        waitingQueueService.updateAllQueueStatusByTime(now, MAX_SERVICE_USABLE_TIME, WaitingQueueStatus.CANCEL);
//    }
//


}
