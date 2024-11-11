package com.hhplus.io.app.concert.domain.service;

import com.hhplus.io.app.concert.domain.SeatInfo;
import com.hhplus.io.app.concert.domain.entity.SeatStatus;
import com.hhplus.io.app.concert.domain.entity.Seat;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatService {

    private final SeatInfo seatInfo;

    public SeatService(SeatInfo seatInfo) {
        this.seatInfo = seatInfo;
    }

    public List<Seat> getAllSeatByConcertDate(Long concertDateId, SeatStatus status) {
        return seatInfo.getAllSeatByConcertDate(concertDateId, status);
    }

    public Seat getSeat(Long seatId) {
        return seatInfo.getSeat(seatId);
    }

    public void updateStatusAndReservationTime(Long seatId, SeatStatus fromStatus, SeatStatus updateStatus) {
        seatInfo.updateStatusAndReservationTime(seatId, fromStatus, updateStatus);
    }

    public void tempReserveSeat(String token, Long userId, Long concertId, Long concertDateId, List<Long> seatList) {
        seatInfo.tempReserveSeat(token, userId, concertId, concertDateId, seatList);
    }
}
