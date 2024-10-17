package com.hhplus.io.concert.web.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record SeatReservationRequest(@Schema(description = "유저 토큰") String token,
                                     @Schema(description = "예약 날짜") LocalDate date,
                                     @Schema(description = "예약 좌석") String seatNo) {
}
