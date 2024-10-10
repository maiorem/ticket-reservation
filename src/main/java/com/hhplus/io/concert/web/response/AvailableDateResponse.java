package com.hhplus.io.concert.web.response;

import java.time.LocalDateTime;
import java.util.List;

public record AvailableDateResponse(int availableSeats, List<AvailableDateDTO> dateList, List<SeatDTO> avalilableSeats) {
}
