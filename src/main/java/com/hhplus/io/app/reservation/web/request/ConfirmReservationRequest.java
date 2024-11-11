package com.hhplus.io.app.reservation.web.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record ConfirmReservationRequest(@Schema(description = "유저 ID") Long userId,
                                        @Schema(description = "결제금액") int payment) {
}
