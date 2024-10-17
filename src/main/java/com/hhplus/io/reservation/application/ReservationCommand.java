package com.hhplus.io.reservation.application;

import com.hhplus.io.concert.application.SeatUseCaseDTO;

import java.time.LocalDateTime;
import java.util.List;

public record ReservationCommand(
        String username,
        String concertName,
        LocalDateTime reservationDate,
        LocalDateTime confirmDate,
        int reservePeople,
        int payMoney,
        List<SeatUseCaseDTO> seatList
) {
    public static ReservationCommand of(String username, String concertName, LocalDateTime reservationDate, LocalDateTime confirmDate, int reservePeople, int payMoney, List<SeatUseCaseDTO> seatList) {
        return new ReservationCommand(username, concertName, reservationDate, confirmDate, reservePeople, payMoney, seatList);
    }
}
