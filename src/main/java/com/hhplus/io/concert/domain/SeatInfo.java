package com.hhplus.io.concert.domain;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class SeatInfo {

    private final SeatRepository seatRepository;

    public SeatInfo(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    public List<Seat> getAllSeatByConcertDate(Long concertDateId, SeatStatus status) {
        return seatRepository.getAllSeatByDate(concertDateId, String.valueOf(status));
    }

    public Seat getSeat(Long seatId) {
        return seatRepository.getSeat(seatId);
    }

    public void updateStatusAndReservationTime(Long seatId, SeatStatus seatStatus, LocalDateTime updateTime) {
        Seat seat = seatRepository.getSeatByStatusWithLock(seatId, String.valueOf(SeatStatus.AVAILABLE));
        seat.updateSeatStatus(String.valueOf(seatStatus));
        seat.updateReservationTime(updateTime);
    }
}
