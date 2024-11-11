package com.hhplus.io.app.reservation.domain.service;

import com.hhplus.io.app.reservation.domain.ReservationInfo;
import com.hhplus.io.app.reservation.domain.ReservationSeatInfo;
import com.hhplus.io.app.reservation.domain.dto.ReservationInfoDTO;
import com.hhplus.io.app.reservation.domain.entity.ReservationSeat;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {

    private final ReservationInfo reservationInfo;
    private final ReservationSeatInfo reservationSeatInfo;

    public ReservationService(ReservationInfo reservationInfo, ReservationSeatInfo reservationSeatInfo) {
        this.reservationInfo = reservationInfo;
        this.reservationSeatInfo = reservationSeatInfo;
    }


    public ReservationInfoDTO saveReservation(Long userId) {
        return reservationInfo.confirmReservation(userId);
    }

    public ReservationSeat saveReservationSeat(Long userId, Long reservationId, Long seatId) {
        return reservationSeatInfo.saveSeat(userId, reservationId, seatId);
    }
}
