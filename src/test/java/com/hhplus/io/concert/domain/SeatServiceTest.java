package com.hhplus.io.concert.domain;

import com.hhplus.io.app.concert.domain.service.SeatService;
import com.hhplus.io.app.concert.domain.entity.Seat;
import com.hhplus.io.app.concert.domain.entity.SeatStatus;
import com.hhplus.io.app.concert.domain.repository.SeatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SeatServiceTest {
    @Mock
    private SeatRepository seatRepository;
    @InjectMocks
    private SeatService seatService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("예약 날짜에 해당하는 예약 가능 좌석 리스트 조회")
    void getAllSeatByConcertDate() {
        //given
        Long concertDateId = 1L;
        Seat seat1 = Seat.builder().concertId(1L).concertDateId(concertDateId).seatId(1L).seatNumber("01").status(String.valueOf(SeatStatus.AVAILABLE)).build();
        Seat seat2 = Seat.builder().concertId(1L).concertDateId(concertDateId).seatId(2L).seatNumber("02").status(String.valueOf(SeatStatus.AVAILABLE)).build();
        Seat seat3 = Seat.builder().concertId(1L).concertDateId(concertDateId).seatId(3L).seatNumber("03").status(String.valueOf(SeatStatus.AVAILABLE)).build();
        when(seatRepository.getSeat(1L)).thenReturn(seat1);
        when(seatRepository.getSeat(2L)).thenReturn(seat2);
        when(seatRepository.getSeat(3L)).thenReturn(seat3);

        //when
        List<Seat> list = seatService.getAllSeatByConcertDate(1L, SeatStatus.AVAILABLE);

        //then
        assertEquals(3, list.size());
    }

    @Test
    @DisplayName("좌석 아이디로 좌석 조회")
    void getSeat() {
        //given
        Long seatId = 1L;
        Seat seat1 = Seat.builder().concertId(1L).concertDateId(1L).seatId(1L).seatNumber("01").status(String.valueOf(SeatStatus.AVAILABLE)).build();
        when(seatRepository.getSeat(1L)).thenReturn(seat1);

        //when
        Seat result = seatService.getSeat(seatId);

        //then
        assertEquals(seat1.getSeatNumber(), result.getSeatNumber());
    }

    @Test
    @DisplayName("좌석 상태 및 예약 날짜 업데이트")
    void updateStatusAndReservationTime() {
        //given
        Long userId = 1L;
        Long seatId = 1L;
        Seat seat1 = Seat.builder().concertId(1L).concertDateId(1L).seatId(1L).seatNumber("01").status(String.valueOf(SeatStatus.AVAILABLE)).build();

        //when
        seatService.updateStatusAndReservationTime(seatId, SeatStatus.TEMP_RESERVED, SeatStatus.AVAILABLE);

        //then
        assertEquals(String.valueOf(SeatStatus.TEMP_RESERVED), seat1.getStatus());
    }
}