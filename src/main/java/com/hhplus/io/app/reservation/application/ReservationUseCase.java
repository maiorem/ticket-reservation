package com.hhplus.io.app.reservation.application;

import com.hhplus.io.app.amount.domain.service.AmountService;
import com.hhplus.io.app.concert.application.SeatUseCaseDTO;
import com.hhplus.io.app.concert.domain.service.ConcertDateService;
import com.hhplus.io.app.concert.domain.service.ConcertService;
import com.hhplus.io.app.concert.domain.service.SeatService;
import com.hhplus.io.app.concert.domain.entity.ConcertDateStatus;
import com.hhplus.io.app.concert.domain.entity.SeatStatus;
import com.hhplus.io.app.concert.domain.entity.Concert;
import com.hhplus.io.app.concert.domain.entity.ConcertDate;
import com.hhplus.io.app.concert.domain.entity.Seat;
import com.hhplus.io.app.reservation.domain.dto.ConfirmReservationInfo;
import com.hhplus.io.app.reservation.domain.dto.ReservationStore;
import com.hhplus.io.app.reservation.domain.event.ReservationEvent;
import com.hhplus.io.app.reservation.domain.event.ReservationEventPublisher;
import com.hhplus.io.app.reservation.domain.service.ReservationSeatService;
import com.hhplus.io.app.reservation.domain.service.ReservationService;
import com.hhplus.io.app.reservation.domain.entity.ReservationSeat;
import com.hhplus.io.app.usertoken.domain.service.UserService;
import com.hhplus.io.app.usertoken.domain.entity.User;
import com.hhplus.io.app.usertoken.domain.service.WaitingQueueService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationUseCase {

    //서비스 수용 인원 설정
    private static final long MAX_PROCESSING_VOLUME = 50;
    
    private final ReservationService reservationService;
    private final SeatService seatService;
    private final WaitingQueueService waitingQueueService;
    private final UserService userService;
    private final ConcertService concertService;
    private final ConcertDateService concertDateService;
    private final AmountService amountService;
    private final ReservationSeatService reservationSeatService;
    private final ReservationEventPublisher reservationEventPublisher;

    public ReservationUseCase(ReservationService reservationService, SeatService seatService, WaitingQueueService waitingQueueService, UserService userService, ConcertService concertService, ConcertDateService concertDateService, AmountService amountService, ReservationSeatService reservationSeatService, ReservationEventPublisher reservationEventPublisher) {
        this.reservationService = reservationService;
        this.seatService = seatService;
        this.waitingQueueService = waitingQueueService;
        this.userService = userService;
        this.concertService = concertService;
        this.concertDateService = concertDateService;
        this.amountService = amountService;
        this.reservationSeatService = reservationSeatService;
        this.reservationEventPublisher = reservationEventPublisher;
    }

    /**
     * 예약 결제 정보 미리보기
     * - 콘서트, 예약 날짜, 예약 인원, 예약 좌석(list), 결제금액
     */
    public ReservationInfo readReservation(Long userId, Long concertId, Long concertDateId, int people, List<Long> seatList) {
        //사용자 조회
        User user = userService.getUser(userId);

        //콘서트 정보 조회
        Concert concert = concertService.getConcert(concertId);

        //콘서트 날짜 조회
        ConcertDate concertDate = concertDateService.getConcertDate(concertDateId);

        List<SeatUseCaseDTO> list = new ArrayList<>();

        int payment = 0;

        //좌석 번호 조회 및 좌석 별 금액 계산
        for (Long seatId : seatList) {
            Seat seat = seatService.getSeat(seatId);
            SeatUseCaseDTO dto = new SeatUseCaseDTO(seat.getSeatId(), seat.getSeatNumber(), SeatStatus.valueOf(seat.getStatus()), seat.getTicketPrice());
            payment += seat.getTicketPrice();
            list.add(dto);
        }

        return ReservationInfo.of(user.getUsername(),
                concert.getConcertName(),
                concertDate.getConcertDate(),
                LocalDateTime.now(),
                people,
                payment,
                list);
    }


    /**
     * 예약확정 내역 저장
     */
    @Transactional
    public ConfirmReservationInfo confirmReservation(Long userId, int payment) {
        //사용자 조회
        User user = userService.getUser(userId);

        //사용자 잔액으로 금액 결제
        amountService.pay(userId, payment);

        //예약 내역 저장
        ReservationStore store = reservationService.confirmReservation(userId);

        List<SeatUseCaseDTO> seatUseCaseDTOList = new ArrayList<>();
        //좌석 예약확정 및 상태 변경
        for (Long seatId : store.seatList()) {
            seatService.updateStatusAndReservationTime(seatId, SeatStatus.TEMP_RESERVED, SeatStatus.CONFIRMED);
            Seat seat = seatService.getSeat(seatId);
            ReservationSeat reservationSeat = reservationSeatService.saveSeat(userId, store.reservationId(), seatId);
            SeatUseCaseDTO dto = SeatUseCaseDTO.of(reservationSeat.getSeatId(), seat.getSeatNumber(), SeatStatus.valueOf(seat.getStatus()), seat.getTicketPrice());
            seatUseCaseDTOList.add(dto);
        }

        //예약 완료한 콘서트 조회
        Concert concert = concertService.getConcert(store.concertId());
        ConcertDate concertDate = concertDateService.getConcertDate(store.concertDateId());

        //예약 날짜에서 사용 가능 좌석 수 업데이트
        int currentSeats = concertDateService.updateAvailableSeats(store.concertDateId(), store.seatList().size());
        int people = store.seatList().size();

        //좌석 수 0이면 예약 날짜 상태 변경
        if (currentSeats < 1) {
            concertDate.updateStatus(String.valueOf(ConcertDateStatus.FILLED));
        }
        ConfirmReservationInfo info
                = ConfirmReservationInfo.of(user.getUsername(),
                        concert.getConcertName(),
                        concertDate.getConcertDate(),
                        LocalDateTime.now(),
                        people,
                        payment,
                        seatUseCaseDTOList);

        //예매 확정 성공 이벤트 처리
        reservationEventPublisher.publish(ReservationEvent.create(info, store.token()));

        return info;

    }


}
