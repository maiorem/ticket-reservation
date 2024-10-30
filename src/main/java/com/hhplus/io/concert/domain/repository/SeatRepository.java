package com.hhplus.io.concert.domain.repository;

import com.hhplus.io.concert.domain.entity.Seat;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository {

    List<Seat> getAllSeatByDate(Long concertDateId, String status);

    Seat getSeat(Long seatId);

    Seat getSeatWithLock(Long seatId);

}
