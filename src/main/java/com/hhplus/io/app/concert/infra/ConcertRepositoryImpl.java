package com.hhplus.io.app.concert.infra;

import com.hhplus.io.app.concert.domain.entity.Concert;
import com.hhplus.io.app.concert.domain.repository.ConcertRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.hhplus.io.app.concert.domain.entity.QConcert.concert;

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
    public Page<Concert> getConcertList(Pageable pageable, String searchKey) {
        BooleanBuilder builder = new BooleanBuilder();
        if(!StringUtils.isEmpty(searchKey)) builder.and(concert.concertName.contains(searchKey));

        List<Concert> result = jpaQueryFactory
                .selectFrom(concert)
                .where(builder)
                .orderBy(concert.concertId.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long count = jpaQueryFactory
                .select(concert.count())
                .from(concert)
                .fetchOne();
        return new PageImpl<>(result, pageable, count != null ? count : 0);
    }

    @Override
    public Page<Concert> getConcertListWithCache(Pageable pageable) {
        List<Concert> result = jpaQueryFactory
                .selectFrom(concert)
                .orderBy(concert.concertId.desc())
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
