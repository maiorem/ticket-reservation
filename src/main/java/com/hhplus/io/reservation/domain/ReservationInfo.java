package com.hhplus.io.reservation.domain;

import com.hhplus.io.reservation.domain.entity.Reservation;
import com.hhplus.io.reservation.persistence.ReservationRepository;
import org.springframework.stereotype.Component;

@Component
public class ReservationInfo {

    private final ReservationRepository reservationRepository;

    public ReservationInfo(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public Reservation confirmReservation(Long userId, Long concertId, Long concertDateId) {
        Reservation builder = Reservation.builder()
                .userId(userId)
                .concertId(concertId)
                .concertDateId(concertDateId)
                .build();
        return reservationRepository.save(builder);
    }
}
