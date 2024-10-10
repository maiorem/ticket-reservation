package com.hhplus.io.reservation.web.response;

import com.hhplus.io.concert.domain.SeatStatus;

import java.time.LocalDateTime;

public record SeatReservationResponse(LocalDateTime tempDate, String seatNum, SeatStatus seatStatus) {
}
