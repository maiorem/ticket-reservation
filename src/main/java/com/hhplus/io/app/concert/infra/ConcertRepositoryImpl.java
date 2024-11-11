package com.hhplus.io.app.concert.infra;

import com.hhplus.io.app.concert.domain.entity.Concert;
import com.hhplus.io.app.concert.domain.repository.ConcertRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.hhplus.io.concert.domain.entity.QConcert.concert;

@Repository
public class ConcertRepositoryImpl implements ConcertRepository {

    private final ConcertJpaRepository jpaRepository;
    private final JPAQueryFactory jpaQueryFactory;

    public ConcertRepositoryImpl(ConcertJpaRepository jpaRepository, JPAQueryFactory jpaQueryFactory) {
        this.jpaRepository = jpaRepository;
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Concert getConcertById(Long concertId) {
        Optional<Concert> optionalConcert = jpaRepository.findByConcertId(concertId);
        return optionalConcert.orElse(null);
    }

    @Override
    public Concert save(Concert concert) {
        return jpaRepository.save(concert);
    }

    @Override
    public Page<Concert> getConcertList(Pageable pageable) {
        List<Concert> result = jpaQueryFactory
                .selectFrom(concert)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long count = jpaQueryFactory
                .select(concert.count())
                .from(concert)
                .fetchOne();
        return new PageImpl<>(result, pageable, count != null ? count : 0);
    }

}
