package com.hhplus.io.app.concert.web.response;


import java.time.LocalDateTime;

public record AvailableDateDTO(Long concertId, Long concertDateId, LocalDateTime availableDate, int availableSeats){
    public static AvailableDateDTO of(Long concertId, Long concertDateId, LocalDateTime availableDate, int availableSeats) {
        return new AvailableDateDTO(concertId, concertDateId, availableDate, availableSeats);
    }
}
