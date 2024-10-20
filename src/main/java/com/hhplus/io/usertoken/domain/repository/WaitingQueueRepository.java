package com.hhplus.io.usertoken.domain.repository;

import com.hhplus.io.usertoken.domain.entity.WaitingQueue;
import com.hhplus.io.usertoken.domain.entity.WaitingQueueStatus;
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
