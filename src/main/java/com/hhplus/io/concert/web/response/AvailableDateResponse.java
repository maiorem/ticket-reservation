package com.hhplus.io.concert.web.response;

import java.util.List;

public record AvailableDateResponse(Long concertId, List<AvailableDateDTO> dateList) {
    public static AvailableDateResponse of(Long concertId, List<AvailableDateDTO> dateList) {
        return new AvailableDateResponse(concertId, dateList);
    }
}
