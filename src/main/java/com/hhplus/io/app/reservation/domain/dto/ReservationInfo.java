package com.hhplus.io.app.reservation.domain.dto;

import java.util.List;

public record ReservationInfo(String token, Long userId, Long concertId, Long concertDateId, Long reservationId, List<Long> seatList) {
    public static ReservationInfo of(String token, Long userId, Long concertId, Long concertDateId, Long reservationId, List<Long> seatList) {
        return new ReservationInfo(token, userId, concertId, concertDateId, reservationId, seatList);
    }
}
