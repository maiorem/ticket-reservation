package com.hhplus.io.reservation.persistence;

import com.hhplus.io.reservation.domain.entity.ReservationSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationSeatJpaRepository extends JpaRepository<ReservationSeat, Long> {
}
