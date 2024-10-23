package com.hhplus.io.concert.web;

import com.hhplus.io.common.interfaces.ApiResponse;
import com.hhplus.io.concert.application.ConcertUseCase;
import com.hhplus.io.concert.application.SeatReserveCommand;
import com.hhplus.io.concert.application.SeatUseCaseDTO;
import com.hhplus.io.concert.domain.entity.ConcertDate;
import com.hhplus.io.concert.domain.entity.Seat;
import com.hhplus.io.concert.domain.entity.SeatStatus;
import com.hhplus.io.concert.web.request.AvailableSeatRequest;
import com.hhplus.io.concert.web.response.*;
import com.hhplus.io.concert.web.request.SeatReservationRequest;
import com.hhplus.io.usertoken.service.UserTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
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

    @PostMapping("/dates/{concertId}")
    @Operation(summary = "예약가능 날짜 조회")
    public ApiResponse<?> getDateList(@PathVariable @Schema(description = "콘서트 ID")  Long concertId) {
        List<AvailableDateDTO> availableDateDTOList = new ArrayList<>();
        List<ConcertDate> concertDateList = concertUseCase.getConcertDate(concertId);
        for (ConcertDate concertDate : concertDateList) {
            AvailableDateDTO dto = AvailableDateDTO.of(concertId, concertDate.getConcertDateId(), concertDate.getConcertDate(), concertDate.getAvailableSeats());
            availableDateDTOList.add(dto);
        }
        AvailableDateResponse response = AvailableDateResponse.of(concertId, availableDateDTOList);
        return ApiResponse.success("data", response);
    }

    @PostMapping("/seats")
    @Operation(summary = "예약가능 좌석 조회")
    public ApiResponse<?> getSeatList(@RequestBody AvailableSeatRequest request) {
        List<SeatDTO> seatDTOList = new ArrayList<>();

        List<Seat> seatList = concertUseCase.getSeats(request.concertDateId());
        for (Seat seat : seatList) {
            SeatDTO dto = SeatDTO.of(
                    seat.getSeatId(),
                    seat.getSeatNumber(),
                    SeatStatus.valueOf(seat.getStatus()),
                    seat.getTicketPrice()
            );
            seatDTOList.add(dto);
        }
        AvailableSeatResponse response = AvailableSeatResponse.of(
                request.concertId(),
                request.concertDateId(),
                seatDTOList
        );
        return ApiResponse.success("data", response);
    }

    @PostMapping("/seat/apply")
    @Operation(summary = "좌석 예약 요청")
    public ApiResponse<?> reservation(@RequestBody SeatReservationRequest request){
        SeatReserveCommand seatReserveCommand = concertUseCase.tempReserveSeat(request.seatList());
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        List<SeatDTO> list = new ArrayList<>();
        for (SeatUseCaseDTO seatUseCaseDTO : seatReserveCommand.seatList()) {
            SeatDTO dto = SeatDTO.of(seatUseCaseDTO.seatId(), seatUseCaseDTO.seatNumber(), SeatStatus.TEMP_RESERVED, seatUseCaseDTO.ticketPrice());
            list.add(dto);
        }
        SeatReservationResponse response = SeatReservationResponse.of(now, list);
        return ApiResponse.success("data", response);
    }

}
