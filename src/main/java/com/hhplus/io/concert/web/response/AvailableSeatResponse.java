package com.hhplus.io.concert.web.response;

import java.util.List;

public record AvailableSeatResponse(Long concertId, Long concertDateId, List<SeatDTO> seatDTOList) {
    public static AvailableSeatResponse of(Long concertId, Long concertDateId,List<SeatDTO> seatDTOList) {
        return new AvailableSeatResponse(concertId, concertDateId, seatDTOList);
    }
}
