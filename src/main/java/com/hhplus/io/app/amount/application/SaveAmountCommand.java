package com.hhplus.io.app.amount.application;

public record SaveAmountCommand(Long userId, int amount) {
    public static SaveAmountCommand of(Long userId, int amount) {
        return new SaveAmountCommand(userId, amount);
    }
}