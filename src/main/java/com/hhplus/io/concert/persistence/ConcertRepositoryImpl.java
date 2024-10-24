package com.hhplus.io.concert.persistence;

import com.hhplus.io.concert.domain.entity.Concert;
import com.hhplus.io.concert.domain.repository.ConcertRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ConcertRepositoryImpl implements ConcertRepository {

    private final ConcertJpaRepository jpaRepository;

    public ConcertRepositoryImpl(ConcertJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
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
}
