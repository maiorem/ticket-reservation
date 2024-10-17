package com.hhplus.io.reservation.domain;

import com.hhplus.io.concert.domain.SeatStatus;
import com.hhplus.io.concert.domain.entity.Seat;
import com.hhplus.io.concert.persistence.SeatRepository;
import com.hhplus.io.reservation.domain.entity.ReservationSeat;
import com.hhplus.io.reservation.persistence.ReservationSeatRepository;
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
class ReservationSeatInfoTest {

    @Mock
    private SeatRepository seatRepository;
    @Mock
    private ReservationSeatRepository reservationSeatRepository;
    @InjectMocks
    private ReservationSeatInfo reservationSeatInfo;

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
        ReservationSeat reservationSeat = reservationSeatInfo.saveSeat(1L, 1L, 1L);

        //then
        assertEquals(seat.getSeatNumber(), seatRepository.getSeat(reservationSeat.getSeatId()).getSeatNumber());
    }
}