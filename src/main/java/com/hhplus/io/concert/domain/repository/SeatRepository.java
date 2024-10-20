package com.hhplus.io.concert.domain.repository;

import com.hhplus.io.concert.domain.entity.Seat;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository {

    List<Seat> getAllSeatByDate(Long concertDateId, String status);

    Seat getSeat(Long seatId);

    Seat getSeatByStatusWithLock(Long seatId, String status);

    Seat saveSeat(Seat seat1);
}
