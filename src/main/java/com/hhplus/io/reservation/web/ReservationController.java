package com.hhplus.io.reservation.web;

import com.hhplus.io.common.interfaces.ApiResponse;
import com.hhplus.io.concert.application.SeatUseCaseDTO;
import com.hhplus.io.concert.domain.entity.SeatStatus;
import com.hhplus.io.concert.web.response.SeatDTO;
import com.hhplus.io.reservation.application.ConfirmReservationCommand;
import com.hhplus.io.reservation.application.ReservationCommand;
import com.hhplus.io.reservation.application.ReservationUseCase;
import com.hhplus.io.reservation.web.request.ConfirmReservationRequest;
import com.hhplus.io.reservation.web.request.ReadReservationRequest;
import com.hhplus.io.reservation.web.response.ConfirmReservationResponse;
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
        ReservationCommand command = reservationUseCase.readReservation(request.userId(), request.concertId(), request.concertDateId(), request.reservedPeople(), request.seatIdList());
        List<SeatDTO> seatDTOList = new ArrayList<>();
        for (SeatUseCaseDTO seatUseCaseDTO : command.seatList()) {
            SeatDTO dto = new SeatDTO(
                    seatUseCaseDTO.seatId(),
                    seatUseCaseDTO.seatNumber(),
                    SeatStatus.TEMP_RESERVED,
                    seatUseCaseDTO.ticketPrice()
            );
            seatDTOList.add(dto);
        }
        ConfirmReservationResponse response = ConfirmReservationResponse.of(
                command.username(),
                command.concertName(),
                command.reservationDate(),
                command.confirmDate(),
                command.reservePeople(),
                command.payMoney(),
                seatDTOList
        );
        return ApiResponse.success("data", response);
    }

    @PostMapping("/seat/confirm")
    @Operation(summary = "예약 확정 (결제)")
    public ApiResponse<?> payment(@RequestBody ConfirmReservationRequest request){

        ConfirmReservationCommand command = reservationUseCase.confirmReservation(
                request.userId(),
                request.payment()
        );
        List<SeatDTO> seatDTOList = new ArrayList<>();
        for (SeatUseCaseDTO seatUseCaseDTO : command.seatList()) {
            SeatDTO dto = new SeatDTO(
                    seatUseCaseDTO.seatId(),
                    seatUseCaseDTO.seatNumber(),
                    SeatStatus.TEMP_RESERVED,
                    seatUseCaseDTO.ticketPrice()
            );
            seatDTOList.add(dto);
        }
        ConfirmReservationResponse response = ConfirmReservationResponse.of(
                command.username(),
                command.concertName(),
                command.reservationDate(),
                command.confirmDate(),
                command.reservePeople(),
                command.payMoney(),
                seatDTOList
        );
        return ApiResponse.success("data", response);
    }

}
