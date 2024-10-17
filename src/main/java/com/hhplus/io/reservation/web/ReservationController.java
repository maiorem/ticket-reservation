package com.hhplus.io.reservation.web;

import com.hhplus.io.common.response.ApiResponse;
import com.hhplus.io.concert.web.response.SeatDTO;
import com.hhplus.io.reservation.application.ReservationUseCase;
import com.hhplus.io.reservation.web.request.ConfirmReservationRequest;
import com.hhplus.io.reservation.web.response.ConfirmReservationResponse;
import com.hhplus.io.usertoken.domain.service.UserTokenService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reserve")
public class ReservationController {

    private final ReservationUseCase reservationUseCase;
    private final UserTokenService userTokenService;

    public ReservationController(ReservationUseCase reservationUseCase, UserTokenService userTokenService) {
        this.reservationUseCase = reservationUseCase;
        this.userTokenService = userTokenService;
    }

    @PostMapping("/seat/view")
    @Operation(summary = "예약 정보 미리보기")
    public ApiResponse<?> readReservaton(@RequestBody ConfirmReservationRequest request){
        //토큰 잔여시간 검사 (만료 시 exception)
//        if(userTokenService.isExpireToken(request.token())) {
//            throw new TokenExpireException("토큰 만료시간을 초과하였습니다.");
//        }
        ConfirmReservationResponse response = new ConfirmReservationResponse(
                "홍세영",
                "박정현 콘서트",
                LocalDateTime.of(2024,10,12, 18, 0, 0),
                LocalDateTime.of(2024, 10, 9, 12, 0, 0),
                1,
                20000,
                List.of(new SeatDTO("01"))
        );
        return ApiResponse.success("data", response);
    }

    @PostMapping("/seat/confirm")
    @Operation(summary = "예약 확정 (결제)")
    public ApiResponse<?> payment(@RequestBody ConfirmReservationRequest request){
        //토큰 잔여시간 검사 (만료 시 exception)
//        if(userTokenService.isExpireToken(request.token())) {
//            throw new TokenExpireException("토큰 만료시간을 초과하였습니다.");
//        }
        ConfirmReservationResponse response = new ConfirmReservationResponse(
                "홍세영",
                "박정현 콘서트",
                LocalDateTime.of(2024,10,12, 18, 0, 0),
                LocalDateTime.of(2024, 10, 9, 12, 0, 0),
                1,
                20000,
                List.of(new SeatDTO("01"))
        );
        return ApiResponse.success("data", response);
    }

}
