package com.hhplus.io.reservation.domain;

import com.hhplus.io.reservation.domain.entity.ReservationSeat;
import com.hhplus.io.reservation.domain.repository.ReservationSeatRepository;
import org.springframework.stereotype.Component;

@Component
public class ReservationSeatInfo {

    private final ReservationSeatRepository reservationSeatRepository;

    public ReservationSeatInfo(ReservationSeatRepository reservationSeatRepository) {
        this.reservationSeatRepository = reservationSeatRepository;
    }


    public ReservationSeat saveSeat(Long userId, Long reservationId, Long seatId) {
        ReservationSeat builder = ReservationSeat.builder()
                .userId(userId)
                .reservationId(reservationId)
                .seatId(seatId).build();
        return reservationSeatRepository.save(builder);
    }
}
