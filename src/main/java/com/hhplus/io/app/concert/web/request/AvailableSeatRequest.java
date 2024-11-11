package com.hhplus.io.app.concert.web.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record AvailableSeatRequest(@Schema(description = "콘서트 ID") Long concertId,
                                   @Schema(description = "콘서트날짜 ID") Long concertDateId) {
    public static AvailableSeatRequest of (Long concertId, Long concertDateId) {
        return new AvailableSeatRequest(concertId, concertDateId);
    }
}
