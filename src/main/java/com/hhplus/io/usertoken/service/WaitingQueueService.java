package com.hhplus.io.usertoken.service;

import com.hhplus.io.usertoken.domain.WaitingQueueInfo;
import com.hhplus.io.usertoken.domain.entity.WaitingQueueStatus;
import com.hhplus.io.usertoken.domain.entity.User;
import com.hhplus.io.usertoken.domain.entity.WaitingQueue;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class WaitingQueueService {

    private final WaitingQueueInfo waitingQueueInfo;

    public WaitingQueueService(WaitingQueueInfo waitingQueueInfo) {
        this.waitingQueueInfo = waitingQueueInfo;
    }

    public WaitingQueue getWaitingQueueByUser(Long userId, WaitingQueueStatus status) {
        return waitingQueueInfo.getQueueByUserAndStatus(userId, status);
    }

    public Long countWaitingQueueByStatus(WaitingQueueStatus status) {
        return waitingQueueInfo.countByQueueStatus(status);
    }

    public Long getSequence(Long userId) {
        return waitingQueueInfo.getWaitingQueueSequence(userId);
    }

    public String createQueue(Long userId) {
        return waitingQueueInfo.createQueue(userId);
    }


    public void updateStatus(WaitingQueue queue, WaitingQueueStatus status) {
        waitingQueueInfo.updateQueueStatus(queue, status);
    }

    public void updateAllWaitingQueue(long updateProcess) {
        waitingQueueInfo.updateAllWaitingQueue(updateProcess);
    }

    public void updateAllQueueStatusByTime(LocalDateTime now, Long plustime, WaitingQueueStatus status) {
        waitingQueueInfo.updateQueueStatusByTime(now, plustime, status);
    }

    public void initQueue(Long userId, String token) {
        waitingQueueInfo.initQueue(userId, token);
    }

    public String getWaitingQueue(Long userId) {
        return waitingQueueInfo.getWaitingQueue(userId);
    }

    public List<String> getWaitingQueueList(long maxProcessingVolume) {
        return waitingQueueInfo.getWaitingQueueList(maxProcessingVolume);
    }

    public void activateAll(List<String> tokenList) {
        waitingQueueInfo.activateAll(tokenList);
    }

    public boolean isActive(String token) {
        return waitingQueueInfo.isActive(token);
    }

    public void refreshToken(String token) {
        waitingQueueInfo.refreshToken(token);
    }

    public void expireToken(String token) {
        waitingQueueInfo.expireToken(token);
    }
}
