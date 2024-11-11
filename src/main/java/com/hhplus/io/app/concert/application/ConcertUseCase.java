package com.hhplus.io.app.concert.application;

import com.hhplus.io.app.concert.domain.service.ConcertDateService;
import com.hhplus.io.app.concert.domain.service.ConcertService;
import com.hhplus.io.app.concert.domain.service.SeatService;
import com.hhplus.io.app.concert.domain.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ConcertUseCase {

    private final ConcertService concertService;
    private final SeatService seatService;
    private final ConcertDateService concertDateService;

    public ConcertUseCase(ConcertService concertService, SeatService seatService, ConcertDateService concertDateService) {
        this.concertService = concertService;
        this.seatService = seatService;
        this.concertDateService = concertDateService;
    }

    /**
     * 콘서트 목록 조회 (페이징)
     */
    public Page<Concert> getConcertList(Pageable pageable) {
        return concertService.getConcertList(pageable);
    }

    /**
     * 선택한 콘서트에서 예약 가능한 날짜 조회
     */
    public List<ConcertDate> getConcertDate(Long concertId) {
        return concertDateService.getAllDateListByConcert(concertId, ConcertDateStatus.AVAILABLE);
    }

    /**
     * 날짜에 해당하는 예약 가능 좌석 조회
     */
    public List<Seat> getSeats(Long concertDateId) {
        return seatService.getAllSeatByConcertDate(concertDateId, SeatStatus.AVAILABLE);
    }

    /**
     * 좌석 예약 (임시 선택)
     * - 결제까지 제한시간 5분.
     */
    public SeatReserveCommand tempReserveSeat(SeatReserveMapper mapper) {

        seatService.tempReserveSeat(mapper.token(), mapper.userId(), mapper.concertId(), mapper.concertDateId(), mapper.seatList());

        List<SeatUseCaseDTO> list = new ArrayList<>();
        for (Long seatId : mapper.seatList()) {
            seatService.updateStatusAndReservationTime(seatId, SeatStatus.AVAILABLE, SeatStatus.TEMP_RESERVED);
            Seat seat = seatService.getSeat(seatId);
            SeatUseCaseDTO dto = SeatUseCaseDTO.of(seat.getSeatId(), seat.getSeatNumber(), SeatStatus.valueOf(seat.getStatus()), seat.getTicketPrice());
            list.add(dto);
        }
        return SeatReserveCommand.of(list);
    }


}
