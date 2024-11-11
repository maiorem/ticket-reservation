package com.hhplus.io.app.concert.application;

import com.hhplus.io.app.concert.web.request.SeatReservationRequest;

import java.util.List;

public record SeatReserveMapper(String token, Long userId, Long concertId, Long concertDateId, List<Long> seatList) {

    public static SeatReserveMapper convert(String token, SeatReservationRequest request) {
        return new SeatReserveMapper(token, request.userId(), request.concertId(), request.concertDateId(), request.seatList());
    }
}
