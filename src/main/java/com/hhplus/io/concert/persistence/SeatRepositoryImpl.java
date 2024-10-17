package com.hhplus.io.concert.persistence;

import com.hhplus.io.concert.domain.entity.Seat;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SeatRepositoryImpl implements SeatRepository {

    private final SeatJpaRepository jpaRepository;

    public SeatRepositoryImpl(SeatJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<Seat> getAllSeatByDate(Long concertDateId, String status) {
        return jpaRepository.findAllByConcertIdAndStatus(concertDateId, status);
    }

    @Override
    public Seat getSeat(Long seatId) {
        Optional<Seat> seat = jpaRepository.findBySeatId(seatId);
        return seat.orElse(null);
    }
}
