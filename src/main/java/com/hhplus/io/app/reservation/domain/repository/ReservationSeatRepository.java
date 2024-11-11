package com.hhplus.io.app.reservation.domain.repository;

import com.hhplus.io.app.reservation.domain.entity.ReservationSeat;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationSeatRepository {
    ReservationSeat save(ReservationSeat builder);
}
