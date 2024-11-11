package com.hhplus.io.app.concert.infra;

import com.hhplus.io.app.concert.domain.entity.Seat;
import com.hhplus.io.app.concert.domain.repository.SeatRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.hhplus.io.concert.domain.entity.QSeat.seat;

@Repository
public class SeatRepositoryImpl implements SeatRepository {

    private final SeatJpaRepository jpaRepository;
    private final JPAQueryFactory jpaQueryFactory;

    public SeatRepositoryImpl(SeatJpaRepository jpaRepository, JPAQueryFactory jpaQueryFactory) {
        this.jpaRepository = jpaRepository;
        this.jpaQueryFactory = jpaQueryFactory;
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


    @Override
    public Seat getSeatWithLock(Long seatId) {
        return jpaQueryFactory
                .selectFrom(seat)
                .where(seat.seatId.eq(seatId))
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .fetchOne();
    }

}
