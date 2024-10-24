package com.hhplus.io.amount.web.response;


public record RetrieveChargeAmountResponse(Long userId, int chartAmount) {
    public static RetrieveChargeAmountResponse of(Long userId, int chartAmount) {
        return new RetrieveChargeAmountResponse(userId, chartAmount);
    }
}
