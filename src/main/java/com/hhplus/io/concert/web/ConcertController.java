package com.hhplus.io.concert.web;

import com.hhplus.io.common.response.ApiResponse;
import com.hhplus.io.concert.application.ConcertUseCase;
import com.hhplus.io.concert.domain.entity.SeatStatus;
import com.hhplus.io.concert.web.request.AvailableDateRequest;
import com.hhplus.io.concert.web.response.AvailableDateDTO;
import com.hhplus.io.concert.web.response.AvailableDateResponse;
import com.hhplus.io.concert.web.response.SeatDTO;
import com.hhplus.io.concert.web.request.SeatReservationRequest;
import com.hhplus.io.concert.web.response.SeatReservationResponse;
import com.hhplus.io.usertoken.service.UserTokenService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/concert")
public class ConcertController {

    private final UserTokenService userTokenService;
    private final ConcertUseCase concertUseCase;

    public ConcertController(UserTokenService userTokenService, ConcertUseCase concertUseCase) {
        this.userTokenService = userTokenService;
        this.concertUseCase = concertUseCase;
    }

    @PostMapping("/date")
    @Operation(summary = "예약가능 날짜 및 좌석 조회")
    public ApiResponse<?> getDate(@RequestBody AvailableDateRequest request) {

//        //토큰 잔여시간 검사 (만료 시 exception)
//        if(userTokenService.isExpireToken(request.token())) {
//            throw new TokenExpireException("토큰 만료시간을 초과하였습니다.");
//        }
        AvailableDateResponse response = new AvailableDateResponse(1, List.of(new AvailableDateDTO(LocalDate.of(2024, 10, 12))), List.of(new SeatDTO("A-01")));
        return ApiResponse.success("data", response);
    }

    @PostMapping("/seat/apply")
    @Operation(summary = "좌석 예약 요청")
    public ApiResponse<?> reservation(@RequestBody SeatReservationRequest request){
        //토큰 잔여시간 검사 (만료 시 exception)
//        if(userTokenService.isExpireToken(request.token())) {
//            throw new TokenExpireException("토큰 만료시간을 초과하였습니다.");
//        }
        SeatReservationResponse response = new SeatReservationResponse(LocalDateTime.of(LocalDate.of(2024, 10, 12), LocalTime.of(12, 00, 00)), "A-01", SeatStatus.TEMP_RESERVED);
        return ApiResponse.success("data", response);
    }
}
