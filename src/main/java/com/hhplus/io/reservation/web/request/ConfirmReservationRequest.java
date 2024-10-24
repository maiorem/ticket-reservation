package com.hhplus.io.reservation.web.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record ConfirmReservationRequest(@Schema(description = "유저 ID") Long userId,
                                        @Schema(description = "콘서트 ID") Long concertId,
                                        @Schema(description = "예약 날짜") Long concertDateId,
                                        @Schema(description = "예약 인원") int people,
                                        @Schema(description = "결제금액") int payment,
                                        @Schema(description = "예약 좌석") List<Long> seatList) {
}
