package com.hhplus.io.reservation.web;

import com.hhplus.io.common.response.ApiResponse;
import com.hhplus.io.concert.domain.SeatStatus;
import com.hhplus.io.concert.web.response.SeatDTO;
import com.hhplus.io.reservation.web.request.ConfirmReservationRequest;
import com.hhplus.io.reservation.web.request.SeatReservationRequest;
import com.hhplus.io.reservation.web.response.ConfirmReservationResponse;
import com.hhplus.io.reservation.web.response.SeatReservationResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/reserve")
public class ReservationController {

    @PostMapping("/seat/apply")
    @Operation(summary = "좌석 예약 요청")
    public ApiResponse<?> reservation(@RequestBody SeatReservationRequest request){
        SeatReservationResponse response = new SeatReservationResponse(LocalDateTime.of(LocalDate.of(2024, 10, 12), LocalTime.of(12, 00, 00)), "A-01", SeatStatus.TEMP_RESERVED);
        return ApiResponse.success("data", response);
    }

    @PostMapping("/seat/confirm")
    @Operation(summary = "예약 확정 (결제)")
    public ApiResponse<?> payment(@RequestBody ConfirmReservationRequest request){
        ConfirmReservationResponse response = new ConfirmReservationResponse(
                "홍세영",
                "박정현 콘서트",
                LocalDate.of(2024,10,12),
                1,
                List.of(new SeatDTO("A-01"))
        );
        return ApiResponse.success("data", response);
    }

}
