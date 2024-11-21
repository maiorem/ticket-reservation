package com.hhplus.io.app.reservation.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.hhplus.io.app.concert.application.SeatUseCaseDTO;

import java.time.LocalDateTime;
import java.util.List;

public record ConfirmReservationInfo(
        String username,
        String concertName,
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime reservationDate,
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime confirmDate,
        int reservePeople,
        int payMoney,
        List<SeatUseCaseDTO> seatList) {
    public static ConfirmReservationInfo of(String username, String concertName, LocalDateTime reservationDate, LocalDateTime confirmDate, int reservePeople, int payMoney, List<SeatUseCaseDTO> seatList) {
        return new ConfirmReservationInfo(username, concertName, reservationDate, confirmDate, reservePeople, payMoney, seatList);
    }
}
