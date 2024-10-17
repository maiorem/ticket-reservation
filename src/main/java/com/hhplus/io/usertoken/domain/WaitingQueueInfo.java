package com.hhplus.io.usertoken.domain;

import com.hhplus.io.usertoken.domain.entity.User;
import com.hhplus.io.usertoken.domain.entity.WaitingQueue;
import com.hhplus.io.usertoken.persistence.WaitingQueueRepository;
import org.springframework.stereotype.Component;

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

    public WaitingQueue createQueue(User user) {
        //WAITING 전체 수에서 맨 끝 순서 배정
        Long sequence = countByQueueStatus(WaitingQueueStatus.WAITING) + 1;

        WaitingQueue builder = WaitingQueue.builder()
                            .sequence(sequence)
                            .userId(user.getUserId())
                            .status(String.valueOf(WaitingQueueStatus.WAITING))
                            .build();
        return waitingQueueRepository.generateQueue(builder);
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

}
