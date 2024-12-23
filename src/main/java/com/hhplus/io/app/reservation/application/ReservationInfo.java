package com.hhplus.io.app.reservation.application;

import com.hhplus.io.app.concert.application.SeatUseCaseDTO;

import java.time.LocalDateTime;
import java.util.List;

public record ReservationInfo(
        String username,
        String concertName,
        LocalDateTime reservationDate,
        LocalDateTime confirmDate,
        int reservePeople,
        int payMoney,
        List<SeatUseCaseDTO> seatList
) {
    public static ReservationInfo of(String username, String concertName, LocalDateTime reservationDate, LocalDateTime confirmDate, int reservePeople, int payMoney, List<SeatUseCaseDTO> seatList) {
        return new ReservationInfo(username, concertName, reservationDate, confirmDate, reservePeople, payMoney, seatList);
    }
}
