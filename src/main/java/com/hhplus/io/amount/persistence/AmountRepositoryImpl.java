package com.hhplus.io.amount.persistence;

import com.hhplus.io.amount.domain.repository.AmountRepository;
import com.hhplus.io.amount.domain.entity.Amount;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.hhplus.io.amount.domain.entity.QAmount.amount1;

@Repository
public class AmountRepositoryImpl implements AmountRepository {

    private final AmountJpaRepository jpaRepository;
    private final JPAQueryFactory jpaQueryFactory;

    public AmountRepositoryImpl(final AmountJpaRepository jpaRepository, final JPAQueryFactory queryFactory) {
        this.jpaRepository = jpaRepository;
        this.jpaQueryFactory = queryFactory;
    }


    @Override
    public Amount getAmountByUser(Long userId) {
        Optional<Amount> optionalAmount = jpaRepository.findByUserId(userId);
        return optionalAmount.orElse(null);
    }

    @Override
    public Amount getAmountByUserWithLock(Long userId) {
        return jpaQueryFactory
                .selectFrom(amount1)
                .where(amount1.userId.eq(userId))
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .fetchOne();
    }
}
