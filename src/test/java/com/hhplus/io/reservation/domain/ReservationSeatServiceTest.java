package com.hhplus.io.reservation.domain;

import com.hhplus.io.app.concert.domain.entity.SeatStatus;
import com.hhplus.io.app.concert.domain.entity.Seat;
import com.hhplus.io.app.concert.domain.repository.SeatRepository;
import com.hhplus.io.app.reservation.domain.service.ReservationSeatService;
import com.hhplus.io.app.reservation.domain.entity.ReservationSeat;
import com.hhplus.io.app.reservation.domain.repository.ReservationSeatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationSeatServiceTest {

    @Mock
    private SeatRepository seatRepository;
    @Mock
    private ReservationSeatRepository reservationSeatRepository;
    @InjectMocks
    private ReservationSeatService reservationSeatService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("예약한 좌석 저장")
    void saveSeat() {
        //given
        Long seatId = 1L;
        Seat seat = Seat.builder().seatId(seatId).seatNumber("01").status(String.valueOf(SeatStatus.AVAILABLE)).build();
        when(seatRepository.getSeat(seatId)).thenReturn(seat);

        //when
        ReservationSeat reservationSeat = reservationSeatService.saveSeat(1L, 1L, 1L);

        //then
        assertEquals(seat.getSeatNumber(), seatRepository.getSeat(reservationSeat.getSeatId()).getSeatNumber());
    }
}