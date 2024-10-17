package com.hhplus.io.concert.application;

public record SeatUseCaseDTO(Long seatId, String seatNumber) {
    public static SeatUseCaseDTO of(Long seatId, String seatNumber) {
        return new SeatUseCaseDTO(seatId, seatNumber);
    }
}
