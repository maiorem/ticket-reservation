package com.hhplus.io.reservation.web.request;

import java.util.List;

public record ReadReservationRequest(Long userId, Long concertId, Long concertDateId, int reservedPeople, List<Long> seatIdList) {
    public static ReadReservationRequest of(Long userId, Long concertId, Long concertDateId, int reservedPeople, List<Long> seatIdList) {
        return new ReadReservationRequest(userId, concertId, concertDateId, reservedPeople, seatIdList);
    }
}
