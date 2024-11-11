package com.hhplus.io.app.concert.web.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record SeatReservationRequest(@Schema(description = "유저 ID") Long userId,
                                    @Schema(description = "콘서트 ID") Long concertId,
                                    @Schema(description = "콘서트날짜 ID") Long concertDateId,
                                    @Schema(description = "예약 좌석 리스트") List<Long> seatList) {
    public static SeatReservationRequest of(Long userId, Long concertId, Long concertDateId, List<Long> seatList) {
        return new SeatReservationRequest(userId, concertId, concertDateId, seatList);
    }

}
