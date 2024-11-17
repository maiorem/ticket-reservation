package com.hhplus.io.app.reservation.domain.dto;

import java.util.List;

public record ReservationStore(String token, Long userId, Long concertId, Long concertDateId, Long reservationId, List<Long> seatList) {
    public static ReservationStore of(String token, Long userId, Long concertId, Long concertDateId, Long reservationId, List<Long> seatList) {
        return new ReservationStore(token, userId, concertId, concertDateId, reservationId, seatList);
    }
}
