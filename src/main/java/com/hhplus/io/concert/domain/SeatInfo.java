package com.hhplus.io.concert.domain;

import com.hhplus.io.concert.domain.entity.Seat;
import com.hhplus.io.concert.domain.entity.SeatStatus;
import com.hhplus.io.concert.domain.repository.SeatRepository;
import com.hhplus.io.support.domain.error.CoreException;
import com.hhplus.io.support.domain.error.ErrorType;
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

    public void updateStatusAndReservationTime(Long seatId, SeatStatus fromStatus, SeatStatus updateStatus, LocalDateTime updateTime) {
        Seat seat = seatRepository.getSeatByStatusWithLock(seatId, String.valueOf(fromStatus));
        if (seat == null) {
            throw new CoreException(ErrorType.SEAT_NOT_FOUND);
        }
        seat.updateSeatStatus(String.valueOf(updateStatus));
        seat.updateReservationTime(updateTime);
    }
}

