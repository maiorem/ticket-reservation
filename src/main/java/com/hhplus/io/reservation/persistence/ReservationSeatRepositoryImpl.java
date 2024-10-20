package com.hhplus.io.reservation.persistence;

import com.hhplus.io.reservation.domain.ReservationSeat;
import com.hhplus.io.reservation.domain.ReservationSeatRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ReservationSeatRepositoryImpl implements ReservationSeatRepository {

    private final ReservationSeatJpaRepository jpaRepository;

    public ReservationSeatRepositoryImpl(ReservationSeatJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public ReservationSeat save(ReservationSeat builder) {
        return jpaRepository.save(builder);
    }
}
