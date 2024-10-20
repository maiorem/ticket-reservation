package com.hhplus.io.reservation.domain.repository;

import com.hhplus.io.reservation.domain.entity.ReservationSeat;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationSeatRepository {
    ReservationSeat save(ReservationSeat builder);
}
