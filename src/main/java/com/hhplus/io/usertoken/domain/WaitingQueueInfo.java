package com.hhplus.io.usertoken.domain;

import com.hhplus.io.usertoken.domain.entity.User;
import com.hhplus.io.usertoken.domain.entity.WaitingQueue;
import com.hhplus.io.usertoken.domain.entity.WaitingQueueStatus;
import com.hhplus.io.usertoken.domain.repository.WaitingQueueRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class WaitingQueueInfo {

    private final WaitingQueueRepository waitingQueueRepository;

    public WaitingQueueInfo(WaitingQueueRepository waitingQueueRepository) {
        this.waitingQueueRepository = waitingQueueRepository;
    }

    public WaitingQueue getQueueByUserAndStatus(Long userId, WaitingQueueStatus status) {
        return waitingQueueRepository.getQueueByUserAndStatus(userId, status);
    }


    private List<WaitingQueue> getAllQueueByStatus(WaitingQueueStatus status) {
        return waitingQueueRepository.getAllQueueByStatus(status);
    }

    public void updateQueueStatus(WaitingQueue queue, WaitingQueueStatus status) {
        queue.udpateStatus(String.valueOf(status));
    }

    public String createQueue(Long userId) {
//        //WAITING 전체 수에서 맨 끝 순서 배정
//        Long sequence = countByQueueStatus(WaitingQueueStatus.WAITING) + 1;
//
//        WaitingQueue builder = WaitingQueue.builder()
//                            .sequence(sequence)
//                            .userId(user.getUserId())
//                            .status(String.valueOf(WaitingQueueStatus.WAITING))
//                            .build();
        return waitingQueueRepository.createWaitingQueue(userId);
    }

    public Long countByQueueStatus(WaitingQueueStatus status) {
        return waitingQueueRepository.countByQueueStatus(status);
    }

    public void updateAllWaitingQueue(long updateProcess) {
        List<WaitingQueue> queueList = waitingQueueRepository.getAllQueueByStatusLimitUpdateCount(WaitingQueueStatus.WAITING, updateProcess);
        for (WaitingQueue queue : queueList) {
            queue.udpateStatus(String.valueOf(WaitingQueueStatus.PROCESS));
        }
        getAllQueueByStatus(WaitingQueueStatus.WAITING).forEach(queue -> queue.updateSequence(updateProcess));

    }

    public void updateQueueStatusByTime(LocalDateTime now, Long plustime, WaitingQueueStatus status) {
        List<WaitingQueue> queueList = waitingQueueRepository.getAllQueueByStatus(WaitingQueueStatus.WAITING);
        for (WaitingQueue queue : queueList) {
            if(now.isAfter(queue.getCreatedAt().plusHours(plustime))) {
                queue.udpateStatus(String.valueOf(status));
            }
        }

    }

    public void initQueue(Long userId, String token) {
        waitingQueueRepository.deleteWaitingQueue(userId, token);
    }

    public String getWaitingQueue(Long userId) {
        return waitingQueueRepository.getWaitingQueue(userId);
    }

    public Long getWaitingQueueSequence(Long userId) {
        return waitingQueueRepository.getWatingQueueSequence(userId);
    }

    public List<String> getWaitingQueueList(long maxProcessingVolume) {
        return waitingQueueRepository.getWaitingQueueList(maxProcessingVolume);
    }

    public void activateAll(List<String> tokenList) {
        waitingQueueRepository.activateAll(tokenList);
    }

    public boolean isActive(String token) {
        return waitingQueueRepository.isActive(token);
    }

    public void refreshToken(String token) {
        waitingQueueRepository.refreshTimeout(token);
    }

    public void expireToken(String token) {
        waitingQueueRepository.deleteActiveToken(token);
    }
}
