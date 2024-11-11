package com.hhplus.io.app.reservation.infra;

import com.hhplus.io.app.reservation.domain.entity.ReservationSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationSeatJpaRepository extends JpaRepository<ReservationSeat, Long> {
}
