package com.hhplus.io.reservation.web.response;

import com.hhplus.io.concert.web.response.SeatDTO;

import java.time.LocalDateTime;
import java.util.List;

public record ConfirmReservationResponse(
        String userName,
        String concertName,
        LocalDateTime reserveDate,
        LocalDateTime confirmDate,
        int reservePeople,
        int payment,
        List<SeatDTO> seatList
) {
    public static ConfirmReservationResponse of(String userName, String concertName, LocalDateTime reserveDate, LocalDateTime confirmDate, int reservePeople, int payment, List<SeatDTO> seatList) {
        return new ConfirmReservationResponse(userName, concertName, reserveDate, confirmDate, reservePeople, payment, seatList);
    }
}
