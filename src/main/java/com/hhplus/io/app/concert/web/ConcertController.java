package com.hhplus.io.app.concert.web;

import com.hhplus.io.app.concert.web.response.AvailableDateDTO;
import com.hhplus.io.app.concert.web.response.AvailableDateResponse;
import com.hhplus.io.app.concert.web.response.AvailableSeatResponse;
import com.hhplus.io.app.concert.web.response.SeatDTO;
import com.hhplus.io.common.interfaces.ApiResponse;
import com.hhplus.io.app.concert.application.ConcertUseCase;
import com.hhplus.io.app.concert.application.SeatReserveCommand;
import com.hhplus.io.app.concert.application.SeatReserveMapper;
import com.hhplus.io.app.concert.application.SeatUseCaseDTO;
import com.hhplus.io.app.concert.domain.entity.Concert;
import com.hhplus.io.app.concert.domain.entity.ConcertDate;
import com.hhplus.io.app.concert.domain.entity.Seat;
import com.hhplus.io.app.concert.domain.entity.SeatStatus;
import com.hhplus.io.app.concert.web.request.AvailableSeatRequest;
import com.hhplus.io.app.concert.web.request.SeatReservationRequest;
import com.hhplus.io.app.usertoken.domain.service.UserTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/list")
    public ApiResponse<?> getConcertList(@PageableDefault(size = 6, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Concert> result = concertUseCase.getConcertList(pageable);
        return ApiResponse.success("data", result);
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
    public ApiResponse<?> reservation(@RequestHeader("token") String token, @RequestBody SeatReservationRequest request){
        SeatReserveCommand seatReserveCommand = concertUseCase.tempReserveSeat(SeatReserveMapper.convert(token, request));
        List<SeatDTO> list = new ArrayList<>();
        for (SeatUseCaseDTO seatUseCaseDTO : seatReserveCommand.seatList()) {
            SeatDTO dto = SeatDTO.of(seatUseCaseDTO.seatId(), seatUseCaseDTO.seatNumber(), SeatStatus.TEMP_RESERVED, seatUseCaseDTO.ticketPrice());
            list.add(dto);
        }
        return ApiResponse.success("data", list);
    }

}
