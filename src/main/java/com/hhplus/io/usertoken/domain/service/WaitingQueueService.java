package com.hhplus.io.usertoken.domain.service;

import com.hhplus.io.usertoken.domain.WaitingQueueInfo;
import com.hhplus.io.usertoken.domain.WaitingQueueStatus;
import com.hhplus.io.usertoken.domain.entity.User;
import com.hhplus.io.usertoken.domain.entity.WaitingQueue;
import org.springframework.stereotype.Service;

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
        WaitingQueue queue = getWaitingQueueByUser(userId, WaitingQueueStatus.WAITING);
        return queue.getSequence();
    }

    public WaitingQueue createQueue(User user) {
        return waitingQueueInfo.createQueue(user);
    }


    public void updateStatus(WaitingQueue queue, WaitingQueueStatus status) {
        waitingQueueInfo.updateQueueStatus(queue, status);
    }

    public void updateAllWaitingQueue(long updateProcess) {
        waitingQueueInfo.updateAllWaitingQueue(updateProcess);
    }

}
