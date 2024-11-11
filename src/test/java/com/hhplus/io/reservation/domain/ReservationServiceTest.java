package com.hhplus.io.reservation.domain;

import com.hhplus.io.app.concert.domain.entity.ConcertDateStatus;
import com.hhplus.io.app.concert.domain.entity.Concert;
import com.hhplus.io.app.concert.domain.entity.ConcertDate;
import com.hhplus.io.app.concert.domain.repository.ConcertDateRepository;
import com.hhplus.io.app.concert.domain.repository.ConcertRepository;
import com.hhplus.io.app.reservation.domain.dto.ReservationInfo;
import com.hhplus.io.app.reservation.domain.service.ReservationService;
import com.hhplus.io.app.reservation.domain.entity.Reservation;
import com.hhplus.io.app.reservation.domain.repository.ReservationRepository;
import com.hhplus.io.app.usertoken.domain.entity.User;
import com.hhplus.io.app.usertoken.domain.repository.UserRepository;
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
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ConcertRepository concertRepository;
    @Mock
    private ConcertDateRepository concertDateRepository;
    @InjectMocks
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("예약 내역 저장")
    void confirmReservation() {
        //given
        Long userId = 1L;
        Long concertId = 1L;
        Long concertDateId = 1L;
        User user = User.builder().userId(userId).username("홍세영").build();
        Concert concert = Concert.builder().concertId(concertId).concertName("조수미 콘서트").theater("국립극장").totalSeats(30).build();
        ConcertDate concertDate = ConcertDate.builder().concertDateId(concertDateId).concertDate(LocalDateTime.now()).concertId(concertId).availableSeats(20).status(String.valueOf(ConcertDateStatus.AVAILABLE)).build();

        when(userRepository.getUser(userId)).thenReturn(user);
        when(concertRepository.getConcertById(concertId)).thenReturn(concert);
        when(concertDateRepository.getConcertDate(concertDateId)).thenReturn(concertDate);

        //when
        ReservationInfo result = reservationService.confirmReservation(userId);

        //then
        assertEquals(user.getUsername(), userRepository.getUser(result.userId()).getUsername());
    }
}