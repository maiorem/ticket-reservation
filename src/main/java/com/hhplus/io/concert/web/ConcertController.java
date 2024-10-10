package com.hhplus.io.concert.web;

import com.hhplus.io.common.response.ApiResponse;
import com.hhplus.io.concert.web.request.AvailableDateRequest;
import com.hhplus.io.concert.web.response.AvailableDateDTO;
import com.hhplus.io.concert.web.response.AvailableDateResponse;
import com.hhplus.io.concert.web.response.SeatDTO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/concert")
public class ConcertController {

    @PostMapping("/date")
    @Operation(summary = "예약가능 날짜 및 좌석 조회")
    public ApiResponse<?> getDate(@RequestBody AvailableDateRequest request) {
        AvailableDateResponse response = new AvailableDateResponse(1, List.of(new AvailableDateDTO(LocalDate.of(2024, 10, 12))), List.of(new SeatDTO("A-01")));
        return ApiResponse.success("data", response);
    }

}
