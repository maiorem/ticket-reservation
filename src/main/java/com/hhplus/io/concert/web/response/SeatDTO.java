package com.hhplus.io.concert.web.response;

import com.hhplus.io.concert.domain.entity.SeatStatus;

public record SeatDTO(Long seatId, String seatNumber, SeatStatus status, int ticketPrice) {
    public static SeatDTO of(Long seatId, String seatNumber, SeatStatus status, int ticketPrice) {
        return new SeatDTO(seatId, seatNumber, status, ticketPrice);
    }
}
