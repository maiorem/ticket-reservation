package com.hhplus.io.concert.domain;

import com.hhplus.io.app.concert.domain.ConcertInfo;
import com.hhplus.io.app.concert.domain.entity.Concert;
import com.hhplus.io.app.concert.domain.repository.ConcertRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConcertInfoTest {
    @Mock
    private ConcertRepository concertRepository;
    @InjectMocks
    private ConcertInfo concertInfo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        concertRepository = mock(ConcertRepository.class);
        concertInfo = new ConcertInfo(concertRepository);
    }

    @Test
    @DisplayName("콘서트 아이디로 콘서트 조회")
    void getConcert() {
        //given
        Long concertId = 1L;
        Concert concert = Concert.builder().concertId(concertId).concertName("조수미 콘서트")
                .startAt(LocalDateTime.of(2024, 10, 20, 18, 0, 0))
                .endAt(LocalDateTime.of(2024, 11, 30, 20, 0, 0))
                .theater("국립극장")
                .totalSeats(40)
                .build();
        when(concertRepository.getConcertById(concertId)).thenReturn(concert);

        //when
        Concert result = concertInfo.getConcert(concertId);

        //then
        assertEquals("조수미 콘서트", result.getConcertName());
    }
}