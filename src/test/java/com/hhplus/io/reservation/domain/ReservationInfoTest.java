package com.hhplus.io.reservation.domain;

import com.hhplus.io.concert.domain.ConcertDateStatus;
import com.hhplus.io.concert.domain.entity.Concert;
import com.hhplus.io.concert.domain.entity.ConcertDate;
import com.hhplus.io.concert.persistence.ConcertDateRepository;
import com.hhplus.io.concert.persistence.ConcertRepository;
import com.hhplus.io.reservation.domain.entity.Reservation;
import com.hhplus.io.reservation.persistence.ReservationRepository;
import com.hhplus.io.usertoken.domain.entity.User;
import com.hhplus.io.usertoken.persistence.UserRepository;
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
class ReservationInfoTest {

    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ConcertRepository concertRepository;
    @Mock
    private ConcertDateRepository concertDateRepository;
    @InjectMocks
    private ReservationInfo reservationInfo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("예약 내역 저장")
    void confirmReservation() {
        //given
        Long reservationId = 1L;
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
        Reservation result = reservationInfo.confirmReservation(userId, concertId, concertDateId);

        //then
        assertEquals(user.getUsername(), userRepository.getUser(result.getUserId()).getUsername());
    }
}