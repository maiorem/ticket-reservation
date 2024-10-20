package com.hhplus.io.reservation.domain.repository;

import com.hhplus.io.reservation.domain.entity.Reservation;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository {
    Reservation save(Reservation builder);
}
