package com.hhplus.io.reservation.web.response;

import com.hhplus.io.concert.web.response.SeatDTO;

import java.time.LocalDate;
import java.util.List;

public record ConfirmReservationResponse(
        String userName,
        String concertName,
        LocalDate reserveDate,
        int reservePeople,
        List<SeatDTO> seatList
) {
}
