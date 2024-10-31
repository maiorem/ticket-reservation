package com.hhplus.io.reservation.application;

import com.hhplus.io.AcceptanceTest;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
class ReservationIntegrationTest extends AcceptanceTest{

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

    @BeforeEach
    void setUp() throws Exception {
        User user1 = User.builder()
                .userId(1L).username("예이츠")
                .build();
        User user2 = User.builder()
                .userId(2L).username("워드워스")
                .build();
        User user3 = User.builder()
                .userId(3L).username("카프카")
                .build();
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        UserToken userToken1 = UserToken.builder()
                .userId(1L)
                .tokenId(1L)
                .token("aaaa")
                .isActive(true)
                .tokenExpire(LocalDateTime.now())
                .build();
        UserToken userToken2 = UserToken.builder()
                .userId(2L)
                .tokenId(2L)
                .token("bbbb")
                .isActive(true)
                .tokenExpire(LocalDateTime.now())
                .build();
        UserToken userToken3 = UserToken.builder()
                .userId(3L)
                .tokenId(3L)
                .token("cccc")
                .isActive(true)
                .tokenExpire(LocalDateTime.now())
                .build();
        userTokenRepository.save(userToken1);
        userTokenRepository.save(userToken2);
        userTokenRepository.save(userToken3);
        WaitingQueue queue1 = WaitingQueue.builder()
                .queueId(1L)
                .userId(1L)
                .sequence(1L)
                .status(String.valueOf(WaitingQueueStatus.PROCESS))
                .build();
        WaitingQueue queue2 = WaitingQueue.builder()
                .queueId(2L)
                .userId(2L)
                .sequence(2L)
                .status(String.valueOf(WaitingQueueStatus.PROCESS))
                .build();
        WaitingQueue queue3 = WaitingQueue.builder()
                .queueId(3L)
                .userId(3L)
                .sequence(3L)
                .status(String.valueOf(WaitingQueueStatus.PROCESS))
                .build();
        waitingQueueRepository.save(queue1);
        waitingQueueRepository.save(queue2);
        waitingQueueRepository.save(queue3);
        Amount amount1 = Amount.builder()
                .amountId(1L)
                .userId(1L)
                .amount(10000)
                .build();
        Amount amount2 = Amount.builder()
                .amountId(2L)
                .userId(2L)
                .amount(20000)
                .build();
        Amount amount3 = Amount.builder()
                .amountId(3L)
                .userId(3L)
                .amount(30000)
                .build();
        amountRepository.save(amount1);
        amountRepository.save(amount2);
        amountRepository.save(amount3);
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
        Seat seat = Seat.builder().concertId(1L).concertDateId(1L).seatId(1L).seatNumber("01").status(String.valueOf(SeatStatus.TEMP_RESERVED)).build();
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

            //when
            ConfirmReservationCommand result = reservationUseCase.confirmReservation(token, userId, 1L, 1L, 1, List.of(1L), 1000);

            //then
            Optional<User> user = userRepository.findByUserId(userId);
            Optional<Concert> concert = concertRepository.findByConcertId(1L);
            Optional<ConcertDate> concertDate = concertDateRepository.findByConcertDateId(1L);
            assertNotNull(result);
            assertEquals(user.get().getUsername(), result.username());
            assertEquals(concert.get().getConcertName(), result.concertName());
        }
        
        @Test
        @DisplayName("[비관적 락] 결제 동시성 테스트")
        void 동일_유저가_결제를_여러번_시도() throws InterruptedException {
            //given
            long startTime = System.nanoTime();
            int threadCount = 3;
            ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
            CountDownLatch latch = new CountDownLatch(threadCount);

            AtomicInteger failureCount = new AtomicInteger(0);
            AtomicInteger successCount = new AtomicInteger(0);

            List<Long> seatList = List.of(1L);
            Long userId = 1L;
            String token = "aaaa";
            int payment = 10000;

            List<ConfirmReservationCommand> commands = new ArrayList<>();

            for (int i = 0; i < threadCount; i++) {
                executorService.submit(() -> {
                    try {
                        ConfirmReservationCommand command = reservationUseCase.confirmReservation(token, userId, 1L, 1L, 1, seatList, payment);
                        commands.add(command);
                        successCount.incrementAndGet();
                    } catch (Exception e) {
                        failureCount.incrementAndGet();
                        System.out.println("Reservation failed: " + e.getMessage());
                    }
                });
                latch.countDown();
            }
            latch.await();
            executorService.shutdown();

            assertNotNull(commands);

            Seat seat = seatRepository.findBySeatId(1L).orElseThrow();
            assertEquals(SeatStatus.CONFIRMED.toString(), seat.getStatus(), "좌석 상태는 CONFIRMED 여야한다.");

            assertEquals(1, successCount.get(), "결제는 한번만 성공해야 한다.");
            assertEquals(threadCount - successCount.get(), failureCount.get(), "성공 1회 외 나머지는 실패해야 한다.");

            Amount amount = amountRepository.findByUserId(userId).orElseThrow();
            assertEquals(0, amount.getAmount(), "잔액은 한번만 지불되어야 한다.");

            long endTime = System.nanoTime();
            long duration = endTime - startTime;

            System.out.println("실행시간 : " + duration + " 나노초");
        }

    }
}