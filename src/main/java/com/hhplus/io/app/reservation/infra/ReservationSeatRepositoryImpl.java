package com.hhplus.io.app.reservation.infra;

import com.hhplus.io.app.reservation.domain.entity.ReservationSeat;
import com.hhplus.io.app.reservation.domain.repository.ReservationSeatRepository;
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
