package com.hhplus.io.usertoken.domain;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WaitingQueueRepository {

    WaitingQueue getQueueByUserAndStatus(Long userId, WaitingQueueStatus status);

    WaitingQueue generateQueue(WaitingQueue builder);

    List<WaitingQueue> getAllQueueByStatus(WaitingQueueStatus waitingQueueStatus);

    Long countByQueueStatus(WaitingQueueStatus status);

    List<WaitingQueue> getAllQueueByStatusLimitUpdateCount(WaitingQueueStatus waitingQueueStatus, long updateProcess);
}
