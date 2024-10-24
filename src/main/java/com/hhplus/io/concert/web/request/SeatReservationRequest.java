package com.hhplus.io.concert.web.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

public record SeatReservationRequest(@Schema(description = "예약 날짜") LocalDateTime date,
                                     @Schema(description = "예약 좌석 리스트") List<Long> seatList) {
    public static SeatReservationRequest of(LocalDateTime date, List<Long> seatList) {
        return new SeatReservationRequest(date, seatList);
    }

}
