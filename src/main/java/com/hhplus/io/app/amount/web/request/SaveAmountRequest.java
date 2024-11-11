package com.hhplus.io.app.amount.web.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record SaveAmountRequest(@Schema(description = "유저 ID") Long userId,
                                @Schema(description = "충전할 금액") int saveAmount) {
    public static SaveAmountRequest of(Long userId, int saveAmount) {
        return new SaveAmountRequest(userId, saveAmount);
    }
}
