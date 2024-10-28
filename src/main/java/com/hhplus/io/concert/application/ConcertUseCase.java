package com.hhplus.io.concert.application;

import com.hhplus.io.concert.domain.entity.ConcertDateStatus;
import com.hhplus.io.concert.domain.entity.SeatStatus;
import com.hhplus.io.concert.domain.entity.ConcertDate;
import com.hhplus.io.concert.domain.entity.Seat;
import com.hhplus.io.concert.service.ConcertDateService;
import com.hhplus.io.concert.service.SeatService;
import com.hhplus.io.support.domain.error.CoreException;
import com.hhplus.io.support.domain.error.ErrorType;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConcertUseCase {

    private final SeatService seatService;
    private final ConcertDateService concertDateService;

    public ConcertUseCase(SeatService seatService, ConcertDateService concertDateService) {
        this.seatService = seatService;
        this.concertDateService = concertDateService;
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
    @Transactional
    public SeatReserveCommand tempReserveSeat(List<Long> seatIdList) {
        List<SeatUseCaseDTO> list = new ArrayList<>();

        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

        for (Long seatId : seatIdList) {
            seatService.updateStatusAndReservationTime(seatId, SeatStatus.AVAILABLE, SeatStatus.TEMP_RESERVED, now);
            Seat seat = seatService.getSeat(seatId);
            SeatUseCaseDTO dto = SeatUseCaseDTO.of(seat.getSeatId(), seat.getSeatNumber(), SeatStatus.valueOf(seat.getStatus()), seat.getTicketPrice());
            list.add(dto);
        }
        return SeatReserveCommand.of(list);
    }

    /**
     * 임시예약 시간 초과 후 좌석 상태 업데이트
     */
    @Transactional
    public void updateSeatStatus(Long seatId) {
        Seat seat = seatService.getSeat(seatId);
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        if (now.isAfter(seat.getReservationTime().plusMinutes(5))) {
            seatService.updateStatusAndReservationTime(seatId, SeatStatus.TEMP_RESERVED, SeatStatus.AVAILABLE, null);
        }

    }

}
