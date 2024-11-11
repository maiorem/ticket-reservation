package com.hhplus.io.app.reservation.infra;

import com.hhplus.io.app.reservation.domain.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationJpaRepository extends JpaRepository<Reservation, Long> {
}
