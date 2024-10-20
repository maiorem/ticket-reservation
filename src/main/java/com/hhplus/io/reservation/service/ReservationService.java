package com.hhplus.io.reservation.service;

import com.hhplus.io.reservation.domain.ReservationInfo;
import com.hhplus.io.reservation.domain.ReservationSeatInfo;
import com.hhplus.io.reservation.domain.Reservation;
import com.hhplus.io.reservation.domain.ReservationSeat;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {

    private final ReservationInfo reservationInfo;
    private final ReservationSeatInfo reservationSeatInfo;

    public ReservationService(ReservationInfo reservationInfo, ReservationSeatInfo reservationSeatInfo) {
        this.reservationInfo = reservationInfo;
        this.reservationSeatInfo = reservationSeatInfo;
    }


    public Reservation saveReservation(Long userId, Long concertId, Long concertDateId) {
        return reservationInfo.confirmReservation(userId, concertId, concertDateId);
    }

    public ReservationSeat saveReservationSeat(Long userId, Long reservationId, Long seatId) {
        return reservationSeatInfo.saveSeat(userId, reservationId, seatId);
    }
}
