package com.hhplus.io.reservation.web.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record ConfirmReservationRequest(@Schema(description = "유저 ID") Long userId,
                                        @Schema(description = "결제금액") int payment) {
}
