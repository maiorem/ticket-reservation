package com.hhplus.io.concert.persistence;

import com.hhplus.io.concert.domain.entity.Seat;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatJpaRepository extends JpaRepository<Seat, Long> {
    List<Seat> findAllByConcertIdAndStatus(Long concertDateId, String status);

    Optional<Seat> findBySeatId(Long seatId);
}
