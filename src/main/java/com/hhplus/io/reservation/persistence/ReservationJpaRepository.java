package com.hhplus.io.reservation.persistence;

import com.hhplus.io.reservation.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationJpaRepository extends JpaRepository<Reservation, Long> {
}
