package com.hhplus.io.app.reservation.domain.repository;

import com.hhplus.io.app.reservation.domain.entity.Reservation;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository {
    Reservation save(Reservation builder);
}
