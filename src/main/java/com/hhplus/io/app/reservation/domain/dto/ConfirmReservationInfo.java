package com.hhplus.io.app.reservation.domain.dto;

import com.hhplus.io.app.concert.application.SeatUseCaseDTO;

import java.time.LocalDateTime;
import java.util.List;

public record ConfirmReservationInfo(
        String username,
        String concertName,
        LocalDateTime reservationDate,
        LocalDateTime confirmDate,
        int reservePeople,
        int payMoney,
        List<SeatUseCaseDTO> seatList) {
    public static ConfirmReservationInfo of(String username, String concertName, LocalDateTime reservationDate, LocalDateTime confirmDate, int reservePeople, int payMoney, List<SeatUseCaseDTO> seatList) {
        return new ConfirmReservationInfo(username, concertName, reservationDate, confirmDate, reservePeople, payMoney, seatList);
    }
}
