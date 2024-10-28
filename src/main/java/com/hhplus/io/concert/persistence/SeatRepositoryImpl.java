package com.hhplus.io.concert.persistence;

import com.hhplus.io.concert.domain.entity.Seat;
import com.hhplus.io.concert.domain.repository.SeatRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
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

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Override
    public Seat getSeatByStatusWithLock(Long seatId, String status) {
       return jpaQueryFactory
               .selectFrom(seat)
               .where(seat.seatId.eq(seatId).and(seat.status.eq(status)))
               .fetchOne();
    }

    @Override
    public Seat saveSeat(Seat seat) {
        return jpaRepository.save(seat);
    }
}
