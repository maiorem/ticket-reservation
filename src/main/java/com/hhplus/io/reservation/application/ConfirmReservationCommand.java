package com.hhplus.io.reservation.application;

import com.hhplus.io.concert.application.SeatUseCaseDTO;

import java.time.LocalDateTime;
import java.util.List;

public record ConfirmReservationCommand(
        String username,
        String concertName,
        LocalDateTime reservationDate,
        LocalDateTime confirmDate,
        int reservePeople,
        int payMoney,
        List<SeatUseCaseDTO> seatList) {
    public static ConfirmReservationCommand of(String username, String concertName, LocalDateTime reservationDate, LocalDateTime confirmDate, int reservePeople, int payMoney, List<SeatUseCaseDTO> seatList) {
        return new ConfirmReservationCommand(username, concertName, reservationDate, confirmDate, reservePeople, payMoney, seatList);
    }
}
