package com.hhplus.io.reservation.domain;

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
