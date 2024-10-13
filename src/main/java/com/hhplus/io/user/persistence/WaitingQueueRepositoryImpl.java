package com.hhplus.io.user.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

@Repository
public class WaitingQueueRepositoryImpl {

    private final WaitingQueueJpaRepository waitingQueueJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;

    public WaitingQueueRepositoryImpl(WaitingQueueJpaRepository waitingQueueJpaRepository, JPAQueryFactory jpaQueryFactory) {
        this.waitingQueueJpaRepository = waitingQueueJpaRepository;
        this.jpaQueryFactory = jpaQueryFactory;
    }
}
