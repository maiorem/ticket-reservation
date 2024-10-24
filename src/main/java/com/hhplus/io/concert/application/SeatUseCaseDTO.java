package com.hhplus.io.concert.application;

import com.hhplus.io.concert.domain.entity.SeatStatus;

public record SeatUseCaseDTO(Long seatId, String seatNumber, SeatStatus status, int ticketPrice) {
    public static SeatUseCaseDTO of(Long seatId, String seatNumber, SeatStatus status, int ticketPrice) {
        return new SeatUseCaseDTO(seatId, seatNumber, status, ticketPrice);
    }
}
