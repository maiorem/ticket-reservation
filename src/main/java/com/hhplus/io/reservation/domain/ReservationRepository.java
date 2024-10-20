package com.hhplus.io.reservation.domain;

import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository {
    Reservation save(Reservation builder);
}
