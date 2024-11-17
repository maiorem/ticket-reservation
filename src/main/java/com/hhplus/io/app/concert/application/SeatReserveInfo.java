package com.hhplus.io.app.concert.application;


import java.util.List;

public record SeatReserveInfo(List<SeatUseCaseDTO> seatList) {
    public static SeatReserveInfo of(List<SeatUseCaseDTO> seatList) {
        return new SeatReserveInfo(seatList);
    }
}
