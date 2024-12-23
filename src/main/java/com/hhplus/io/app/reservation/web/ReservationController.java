package com.hhplus.io.app.reservation.web;

import com.hhplus.io.common.interfaces.ApiResponse;
import com.hhplus.io.app.concert.application.SeatUseCaseDTO;
import com.hhplus.io.app.concert.domain.entity.SeatStatus;
import com.hhplus.io.app.concert.web.response.SeatDTO;
import com.hhplus.io.app.reservation.domain.dto.ConfirmReservationInfo;
import com.hhplus.io.app.reservation.application.ReservationInfo;
import com.hhplus.io.app.reservation.application.ReservationUseCase;
import com.hhplus.io.app.reservation.web.request.ConfirmReservationRequest;
import com.hhplus.io.app.reservation.web.request.ReadReservationRequest;
import com.hhplus.io.app.reservation.web.response.ConfirmReservationResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/reserve")
public class ReservationController {

    private final ReservationUseCase reservationUseCase;

    public ReservationController(ReservationUseCase reservationUseCase) {
        this.reservationUseCase = reservationUseCase;
    }

    @PostMapping("/seat/view")
    @Operation(summary = "예약 정보 미리보기")
    public ApiResponse<?> readReservaton(@RequestBody ReadReservationRequest request){
        ReservationInfo info = reservationUseCase.readReservation(request.userId(), request.concertId(), request.concertDateId(), request.reservedPeople(), request.seatIdList());
        List<SeatDTO> seatDTOList = new ArrayList<>();
        for (SeatUseCaseDTO seatUseCaseDTO : info.seatList()) {
            SeatDTO dto = new SeatDTO(
                    seatUseCaseDTO.seatId(),
                    seatUseCaseDTO.seatNumber(),
                    SeatStatus.TEMP_RESERVED,
                    seatUseCaseDTO.ticketPrice()
            );
            seatDTOList.add(dto);
        }
        ConfirmReservationResponse response = ConfirmReservationResponse.of(
                info.username(),
                info.concertName(),
                info.reservationDate(),
                info.confirmDate(),
                info.reservePeople(),
                info.payMoney(),
                seatDTOList
        );
        return ApiResponse.success("data", response);
    }

    @PostMapping("/seat/confirm")
    @Operation(summary = "예약 확정 (결제)")
    public ApiResponse<?> payment(@RequestBody ConfirmReservationRequest request){

        ConfirmReservationInfo info = reservationUseCase.confirmReservation(
                request.userId(),
                request.payment()
        );
        List<SeatDTO> seatDTOList = new ArrayList<>();
        for (SeatUseCaseDTO seatUseCaseDTO : info.seatList()) {
            SeatDTO dto = new SeatDTO(
                    seatUseCaseDTO.seatId(),
                    seatUseCaseDTO.seatNumber(),
                    SeatStatus.TEMP_RESERVED,
                    seatUseCaseDTO.ticketPrice()
            );
            seatDTOList.add(dto);
        }
        ConfirmReservationResponse response = ConfirmReservationResponse.of(
                info.username(),
                info.concertName(),
                info.reservationDate(),
                info.confirmDate(),
                info.reservePeople(),
                info.payMoney(),
                seatDTOList
        );
        return ApiResponse.success("data", response);
    }

}
