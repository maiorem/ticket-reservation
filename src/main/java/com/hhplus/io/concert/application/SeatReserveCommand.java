package com.hhplus.io.concert.application;


import java.util.List;

public record SeatReserveCommand(List<SeatUseCaseDTO> seatList) {
    public static SeatReserveCommand of(List<SeatUseCaseDTO> seatList) {
        return new SeatReserveCommand(seatList);
    }
}
