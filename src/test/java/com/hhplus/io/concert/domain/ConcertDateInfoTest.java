package com.hhplus.io.concert.domain;

import com.hhplus.io.app.concert.domain.ConcertDateInfo;
import com.hhplus.io.app.concert.domain.entity.ConcertDate;
import com.hhplus.io.app.concert.domain.entity.ConcertDateStatus;
import com.hhplus.io.app.concert.domain.repository.ConcertDateRepository;
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
class ConcertDateInfoTest {

    @Mock
    private ConcertDateRepository concertDateRepository;
    @InjectMocks
    private ConcertDateInfo concertDateInfo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        concertDateRepository = mock(ConcertDateRepository.class);
        concertDateInfo = new ConcertDateInfo(concertDateRepository);
    }

    @Test
    @DisplayName("콘서트에 해당하는 모든 예약 가능한 날짜 리스트 조회")
    void getAllDateListByConcert() {
        //given
        ConcertDate date1 = ConcertDate.builder().concertDateId(1L).concertDate(LocalDateTime.of(2024, 10, 20, 18, 0, 0))
                .concertId(1L).availableSeats(20).status(String.valueOf(ConcertDateStatus.AVAILABLE)).build();
        ConcertDate date2 = ConcertDate.builder().concertDateId(2L).concertDate(LocalDateTime.of(2024, 10, 21, 18, 0, 0))
                .concertId(1L).availableSeats(20).status(String.valueOf(ConcertDateStatus.AVAILABLE)).build();
        ConcertDate date3 = ConcertDate.builder().concertDateId(3L).concertDate(LocalDateTime.of(2024, 10, 22, 18, 0, 0))
                .concertId(1L).availableSeats(20).status(String.valueOf(ConcertDateStatus.AVAILABLE)).build();
        when(concertDateRepository.getConcertDate(1L)).thenReturn(date1);
        when(concertDateRepository.getConcertDate(2L)).thenReturn(date2);
        when(concertDateRepository.getConcertDate(3L)).thenReturn(date3);
        //when
        List<ConcertDate> list = concertDateInfo.getAllDateListByConcert(1L, ConcertDateStatus.AVAILABLE);

        //then
        assertEquals(3, list.size());
    }

    @Test
    @DisplayName("아이디로 콘서트 날짜 조회")
    void getConcerDate() {
        //given
        Long id = 1L;
        ConcertDate date1 = ConcertDate.builder().concertDateId(1L).concertDate(LocalDateTime.of(2024, 10, 22, 18, 0, 0))
                .concertId(1L).availableSeats(20).status(String.valueOf(ConcertDateStatus.AVAILABLE)).build();
        when(concertDateRepository.getConcertDate(1L)).thenReturn(date1);

        //when
        ConcertDate result = concertDateInfo.getConcerDate(id);

        //then
        assertEquals(date1.getConcertDate(), result.getConcertDate());
    }
}