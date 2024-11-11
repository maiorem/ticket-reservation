package com.hhplus.io.app.concert.infra;

import com.hhplus.io.app.concert.domain.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatJpaRepository extends JpaRepository<Seat, Long> {
    List<Seat> findAllByConcertIdAndStatus(Long concertDateId, String status);

    Optional<Seat> findBySeatId(Long seatId);
}
