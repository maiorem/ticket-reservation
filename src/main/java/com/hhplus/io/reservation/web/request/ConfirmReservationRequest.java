package com.hhplus.io.reservation.web.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

public record ConfirmReservationRequest(@Schema(description = "유저 토큰") String token,
                                        @Schema(description = "콘서트 ID") int concertId,
                                        @Schema(description = "예약 날짜") LocalDate date,
                                        @Schema(description = "예약 인원") int people,
                                        @Schema(description = "예약 좌석") List<String> seatNumList) {
}
