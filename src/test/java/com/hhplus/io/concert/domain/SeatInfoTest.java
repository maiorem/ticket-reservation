package com.hhplus.io.concert.domain;

import com.hhplus.io.concert.domain.entity.Seat;
import com.hhplus.io.concert.persistence.SeatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SeatInfoTest {
    @Mock
    private SeatRepository seatRepository;
    @InjectMocks
    private SeatInfo seatInfo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        seatRepository = mock(SeatRepository.class);
        seatInfo = new SeatInfo(seatRepository);
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
        List<Seat> list = seatInfo.getAllSeatByConcertDate(1L, SeatStatus.AVAILABLE);

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
        Seat result = seatInfo.getSeat(seatId);

        //then
        assertEquals(seat1.getSeatNumber(), result.getSeatNumber());
    }

    @Test
    @DisplayName("좌석 상태 및 예약 날짜 업데이트")
    void updateStatusAndReservationTime() {
        //given
        Long seatId = 1L;
        Seat seat1 = Seat.builder().concertId(1L).concertDateId(1L).seatId(1L).seatNumber("01").status(String.valueOf(SeatStatus.AVAILABLE)).build();

        //when
        seatInfo.updateStatusAndReservationTime(seatId, SeatStatus.TEMP_RESERVED, LocalDateTime.now());

        //then
        assertEquals(String.valueOf(SeatStatus.TEMP_RESERVED), seat1.getStatus());
    }
}