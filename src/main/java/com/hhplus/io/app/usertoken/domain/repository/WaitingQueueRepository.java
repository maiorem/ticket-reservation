package com.hhplus.io.app.usertoken.domain.repository;

import com.hhplus.io.app.usertoken.domain.entity.WaitingQueue;
import com.hhplus.io.app.usertoken.domain.entity.WaitingQueueStatus;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WaitingQueueRepository {

    WaitingQueue getQueueByUserAndStatus(Long userId, WaitingQueueStatus status);

    WaitingQueue generateQueue(WaitingQueue builder);

    List<WaitingQueue> getAllQueueByStatus(WaitingQueueStatus waitingQueueStatus);

    Long countByQueueStatus(WaitingQueueStatus status);

    List<WaitingQueue> getAllQueueByStatusLimitUpdateCount(WaitingQueueStatus waitingQueueStatus, long updateProcess);

    String createWaitingQueue(String token);

    Long getWatingQueueRank(String token);

    String getWaitingQueue(String token);

    List<String> getWaitingQueueList(Long range);

    void deleteWaitingQueue(String token);

    void createActiveQueue(String token);

    Boolean isActive(String token);

    void refreshTimeout(String token);

    void deleteActiveToken(String token);

    Long getQueueCount(String key);

    void activateAll(List<String> tokenList);

}
