package com.hhplus.io.app.amount.application;

public record SaveAmountInfo(Long userId, int amount) {
    public static SaveAmountInfo of(Long userId, int amount) {
        return new SaveAmountInfo(userId, amount);
    }
}
