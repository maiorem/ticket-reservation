package com.hhplus.io.app.reservation.domain.dto;

import java.util.List;

public record ReservationInfoDTO(String token, Long userId, Long concertId, Long concertDateId, Long reservationId, List<Long> seatList) {
    public static ReservationInfoDTO of(String token, Long userId, Long concertId, Long concertDateId, Long reservationId, List<Long> seatList) {
        return new ReservationInfoDTO(token, userId, concertId, concertDateId, reservationId, seatList);
    }
}
