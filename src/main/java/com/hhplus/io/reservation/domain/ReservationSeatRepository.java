package com.hhplus.io.reservation.domain;

import org.springframework.stereotype.Repository;

@Repository
public interface ReservationSeatRepository {
    ReservationSeat save(ReservationSeat builder);
}
