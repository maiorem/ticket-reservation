package com.hhplus.io.reservation.application;

import com.hhplus.io.DatabaseCleanUp;
import com.hhplus.io.amount.domain.entity.Amount;
import com.hhplus.io.amount.persistence.AmountJpaRepository;
import com.hhplus.io.concert.domain.entity.ConcertDateStatus;
import com.hhplus.io.concert.domain.entity.SeatStatus;
import com.hhplus.io.concert.domain.entity.Concert;
import com.hhplus.io.concert.domain.entity.ConcertDate;
import com.hhplus.io.concert.domain.entity.Seat;
import com.hhplus.io.concert.persistence.ConcertDateJpaRepository;
import com.hhplus.io.concert.persistence.ConcertJpaRepository;
import com.hhplus.io.concert.persistence.SeatJpaRepository;
import com.hhplus.io.usertoken.domain.entity.UserToken;
import com.hhplus.io.usertoken.domain.entity.WaitingQueueStatus;
import com.hhplus.io.usertoken.domain.entity.User;
import com.hhplus.io.usertoken.domain.entity.WaitingQueue;
import com.hhplus.io.usertoken.persistence.UserJpaRepository;
import com.hhplus.io.usertoken.persistence.UserTokenJpaRepository;
import com.hhplus.io.usertoken.persistence.WaitingQueueJpaRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class ReservationIntegrationTest {

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
    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @BeforeEach
    void setUp() throws Exception {
        User user = User.builder()
                .userId(1L).username("홍세영")
                .build();
        userRepository.save(user);
        UserToken userToken = UserToken.builder()
                .userId(1L)
                .tokenId(1L)
                .token("aaaa")
                .isActive(true)
                .tokenExpire(LocalDateTime.now())
                .build();
        userTokenRepository.save(userToken);
        WaitingQueue queue1 = WaitingQueue.builder()
                .queueId(2L)
                .userId(1L)
                .sequence(1L)
                .status(String.valueOf(WaitingQueueStatus.PROCESS))
                .build();
        waitingQueueRepository.save(queue1);
        Amount amount = Amount.builder()
                .amountId(1L)
                .userId(1L)
                .amount(10000)
                .build();
        amountRepository.save(amount);
        Concert concert = Concert.builder().concertId(1L).concertName("조수미 콘서트")
                .startAt(LocalDateTime.of(2024, 10, 20, 18, 0, 0))
                .endAt(LocalDateTime.of(2024, 11, 30, 20, 0, 0))
                .theater("국립극장")
                .totalSeats(40)
                .build();
        concertRepository.save(concert);
        ConcertDate date = ConcertDate.builder().concertDateId(1L).concertDate(LocalDateTime.of(2024, 10, 20, 18, 0, 0))
                .concertId(1L).availableSeats(20).status(String.valueOf(ConcertDateStatus.AVAILABLE)).build();
        concertDateRepository.save(date);
        Seat seat = Seat.builder().concertId(1L).concertDateId(1L).seatId(1L).seatNumber("01").status(String.valueOf(SeatStatus.AVAILABLE)).build();
        seatRepository.save(seat);
    }

    @Nested
    @DisplayName("예약 결제 확정")
    class confirmReservation {
        
        @Test
        void 결제_성공() {
            //given
            Long userId = 1L;
            String token = "aaaa";
            Optional<User> user = userRepository.findByUserId(userId);
            Optional<Concert> concert = concertRepository.findByConcertId(1L);
            Optional<ConcertDate> concertDate = concertDateRepository.findByConcertDateId(1L);

            //when
            ConfirmReservationCommand result = reservationUseCase.confirmReservation(token, userId, concert.get().getConcertId(), concertDate.get().getConcertDateId(), 1, List.of(1L), 1000);

            //then
            assertNotNull(result);
            assertEquals(user.get().getUsername(), result.username());
            assertEquals(concert.get().getConcertName(), result.concertName());
        }
        
        @Test
        @DisplayName("결제 동시성 테스트")
        void 세사람이_동시에_한_좌석_결제_시_한_사람만_성공() throws InterruptedException {
            //given
            String token = "aaaa";

            int threadCount = 3;
            ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
            CountDownLatch latch = new CountDownLatch(threadCount);

            AtomicInteger failureCount = new AtomicInteger(0);
            AtomicInteger successCount = new AtomicInteger(0);

            List<Long> seatList = List.of(1L);
            List<Long> userIdList = List.of(1L, 2L, 3L);

            for (int i = 0; i < 3; i++) {
                final int index = i;
                executorService.submit(() -> {
                    try {
                        latch.await();
                        Long userId = userIdList.get(index);
                        reservationUseCase.confirmReservation(token, userId, 1L, 1L, 1, seatList, 10000);
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

            Optional<Seat> seat = seatRepository.findBySeatId(1L);
            assertEquals(seat.get().getStatus(), String.valueOf(SeatStatus.CONFIRMED)); // 예약 성공

            assertEquals(1, successCount.get()); // 예약 성공 1개
            assertEquals(2, failureCount.get()); // 예약 실패 2개
        }

    }
    @AfterEach
    void cleanUp(){
        databaseCleanUp.execute();
    }
}