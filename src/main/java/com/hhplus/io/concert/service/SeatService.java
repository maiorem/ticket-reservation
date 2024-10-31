package com.hhplus.io.concert.service;

import com.hhplus.io.concert.domain.SeatInfo;
import com.hhplus.io.concert.domain.entity.SeatStatus;
import com.hhplus.io.concert.domain.entity.Seat;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public void updateStatusAndReservationTime(Long seatId, SeatStatus fromStatus, SeatStatus updateStatus, LocalDateTime time) {
        seatInfo.updateStatusAndReservationTime(seatId, fromStatus, updateStatus, time);
    }

    public void updateStatus(Long seatId, SeatStatus fromStatus, SeatStatus updateStatus, LocalDateTime updateTime) {
        seatInfo.updateStatus(seatId, fromStatus, updateStatus, updateTime);
    }

}
