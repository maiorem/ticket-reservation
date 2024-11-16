package com.hhplus.io.reservation.application;

import com.hhplus.io.app.reservation.domain.dto.ConfirmReservationInfo;
import com.hhplus.io.app.reservation.application.ReservationUseCase;
import com.hhplus.io.testcontainer.AcceptanceTest;
import com.hhplus.io.app.amount.infra.AmountJpaRepository;
import com.hhplus.io.app.concert.domain.entity.SeatStatus;
import com.hhplus.io.app.concert.domain.entity.Concert;
import com.hhplus.io.app.concert.domain.entity.ConcertDate;
import com.hhplus.io.app.concert.domain.entity.Seat;
import com.hhplus.io.app.concert.infra.ConcertDateJpaRepository;
import com.hhplus.io.app.concert.infra.ConcertJpaRepository;
import com.hhplus.io.app.concert.infra.SeatJpaRepository;
import com.hhplus.io.app.usertoken.domain.entity.User;
import com.hhplus.io.app.usertoken.infra.UserJpaRepository;
import com.hhplus.io.app.usertoken.infra.UserTokenJpaRepository;
import com.hhplus.io.app.usertoken.infra.WaitingQueueJpaRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
class ReservationIntegrationTest  extends AcceptanceTest {

    @Autowired
    private ReservationUseCase reservationUseCase;
    @Autowired
    private UserJpaRepository userRepository;
    @Autowired
    private UserTokenJpaRepository userTokenRepository;
    @Autowired
    private AmountJpaRepository amountRepository;
    @Autowired
    private ConcertJpaRepository concertRepository;
    @Autowired
    private SeatJpaRepository seatRepository;
    @Autowired
    private ConcertDateJpaRepository concertDateRepository;
    @Autowired
    private WaitingQueueJpaRepository waitingQueueRepository;

    @Nested
    @DisplayName("예약 결제 확정")
    class confirmReservation {
        
        @Test
        void 결제_성공() {
            //given
            Long userId = 1L;
            int payment = 10000;

            //when
            ConfirmReservationInfo result = reservationUseCase.confirmReservation(userId, payment);

            //then
            Optional<User> user = userRepository.findByUserId(userId);
            Optional<Concert> concert = concertRepository.findByConcertId(1L);
            Optional<ConcertDate> concertDate = concertDateRepository.findByConcertDateId(1L);
            assertNotNull(result);
            assertEquals(user.get().getUsername(), result.username());
            assertEquals(concert.get().getConcertName(), result.concertName());
        }
        
        @Test
        @DisplayName("결제 동시성 테스트")
        void 여러사람이_동시에_한_좌석_결제_시_한_사람만_성공() throws InterruptedException {
            //given
            int threadCount = 3;
            ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
            CountDownLatch latch = new CountDownLatch(threadCount);

            AtomicInteger failureCount = new AtomicInteger(0);
            AtomicInteger successCount = new AtomicInteger(0);

            List<Long> seatList = List.of(1L);
            List<Long> userIdList = List.of(1L, 2L, 3L);
            List<String> tokenList = List.of("aaaa", "bbbb", "cccc");

            for (int i = 0; i < threadCount; i++) {
                final int index = i;
                Long userId = userIdList.get(index);
                executorService.submit(() -> {
                    try {
                        latch.await();
                        reservationUseCase.confirmReservation(userId, 10000);
                        successCount.incrementAndGet();
                    } catch (Exception e) {
                        failureCount.incrementAndGet();
                        System.out.println("Reservation failed: " + e.getMessage());
                    }
                });
                latch.countDown();
            }

            executorService.shutdown();
            executorService.awaitTermination(1, TimeUnit.MINUTES);

            Seat seat = seatRepository.findBySeatId(1L).orElse(null);
            assertEquals(seat.getStatus(), String.valueOf(SeatStatus.CONFIRMED)); // 예약 성공

            assertEquals(1, successCount.get()); // 예약 성공 1개
            assertEquals(threadCount - successCount.get(), failureCount.get()); // 나머지 예약 실패
        }

    }
}