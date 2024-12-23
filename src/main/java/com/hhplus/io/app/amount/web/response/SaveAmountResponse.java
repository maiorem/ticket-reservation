package com.hhplus.io.app.amount.web.response;

public record SaveAmountResponse(Long userId, int totAmount) {
    public static SaveAmountResponse of(Long userId,int totAmount) {
        return new SaveAmountResponse(userId, totAmount);
    }
}
