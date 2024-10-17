package com.hhplus.io.concert.persistence;

import com.hhplus.io.concert.domain.entity.Seat;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository {

    List<Seat> getAllSeatByDate(Long concertDateId, String status);

    Seat getSeat(Long seatId);
}
