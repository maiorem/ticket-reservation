package com.hhplus.io.usertoken.persistence;

import com.hhplus.io.usertoken.domain.repository.WaitingQueueRepository;
import com.hhplus.io.usertoken.domain.entity.WaitingQueueStatus;
import com.hhplus.io.usertoken.domain.entity.WaitingQueue;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.hhplus.io.usertoken.domain.entity.QWaitingQueue.waitingQueue;

@Repository
public class WaitingQueueRepositoryImpl implements WaitingQueueRepository {

    private final WaitingQueueJpaRepository waitingQueueJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;

    public WaitingQueueRepositoryImpl(WaitingQueueJpaRepository waitingQueueJpaRepository, JPAQueryFactory jpaQueryFactory) {
        this.waitingQueueJpaRepository = waitingQueueJpaRepository;
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public WaitingQueue getQueueByUserAndStatus(Long userId, WaitingQueueStatus status) {
        Optional<WaitingQueue> optionalWaitingQueue = waitingQueueJpaRepository.findByUserIdAndStatus(userId, String.valueOf(status));
        return optionalWaitingQueue.orElse(null);
    }

    @Override
    public List<WaitingQueue> getAllQueueByStatus(WaitingQueueStatus status) {
        return waitingQueueJpaRepository.findAllByStatus(String.valueOf(status));
    }

    @Override
    public WaitingQueue generateQueue(WaitingQueue builder) {
        return waitingQueueJpaRepository.save(builder);
    }


    @Override
    public Long countByQueueStatus(WaitingQueueStatus status) {
        return waitingQueueJpaRepository.countByStatus(String.valueOf(status));
    }


    @Override
    public List<WaitingQueue> getAllQueueByStatusLimitUpdateCount(WaitingQueueStatus queueStatus, long updateProcess) {
        return jpaQueryFactory
                .selectFrom(waitingQueue)
                .where(waitingQueue.status.eq(String.valueOf(queueStatus)))
                .limit(updateProcess)
                .fetch();
    }

}
