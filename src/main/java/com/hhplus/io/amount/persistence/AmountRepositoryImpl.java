package com.hhplus.io.amount.persistence;

import com.hhplus.io.amount.domain.entity.Amount;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.Optional;

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
    public Amount save(Amount amount) {
        return jpaRepository.save(amount);
    }
}
