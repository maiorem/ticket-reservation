package com.hhplus.io.app.concert.domain.repository;

import com.hhplus.io.app.concert.domain.entity.Seat;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository {

    List<Seat> getAllSeatByDate(Long concertDateId, String status);

    Seat getSeat(Long seatId);

    Seat getSeatWithLock(Long seatId);

}
