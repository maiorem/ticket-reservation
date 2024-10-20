package com.hhplus.io.concert.persistence;

import com.hhplus.io.concert.domain.entity.ConcertDate;
import com.hhplus.io.concert.domain.repository.ConcertDateRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ConcertDateRepositoryImpl implements ConcertDateRepository {

    private final ConcertDateJpaRepository jpaRepository;

    public ConcertDateRepositoryImpl(ConcertDateJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<ConcertDate> getAllDateListByConcert(Long concertId, String status) {
        return jpaRepository.findAllByConcertIdAndStatus(concertId, status);
    }

    @Override
    public ConcertDate getConcertDate(Long concertDateId) {
        Optional<ConcertDate> concertDate = jpaRepository.findByConcertDateId(concertDateId);
        return concertDate.orElse(null);
    }

    @Override
    public ConcertDate save(ConcertDate date) {
        return jpaRepository.save(date);
    }

}
