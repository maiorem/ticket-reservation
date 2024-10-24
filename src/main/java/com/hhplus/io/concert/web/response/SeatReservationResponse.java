package com.hhplus.io.concert.web.response;

import java.time.LocalDateTime;
import java.util.List;

public record SeatReservationResponse(LocalDateTime tempDate, List<SeatDTO> seatDTOList) {
    public static SeatReservationResponse of(LocalDateTime tempDate, List<SeatDTO> seatDTOList) {
        return new SeatReservationResponse(tempDate, seatDTOList);
    }
}
