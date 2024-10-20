package com.hhplus.io.reservation.application;

import com.hhplus.io.amount.domain.Amount;
import com.hhplus.io.amount.domain.AmountRepository;
import com.hhplus.io.concert.domain.ConcertDateStatus;
import com.hhplus.io.concert.domain.SeatStatus;
import com.hhplus.io.concert.domain.Concert;
import com.hhplus.io.concert.domain.ConcertDate;
import com.hhplus.io.concert.domain.Seat;
import com.hhplus.io.concert.domain.ConcertDateRepository;
import com.hhplus.io.concert.domain.ConcertRepository;
import com.hhplus.io.concert.domain.SeatRepository;
import com.hhplus.io.usertoken.domain.WaitingQueueStatus;
import com.hhplus.io.usertoken.domain.User;
import com.hhplus.io.usertoken.domain.WaitingQueue;
import com.hhplus.io.usertoken.domain.UserRepository;
import com.hhplus.io.usertoken.domain.WaitingQueueRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReservationUseCaseTest {

    @Autowired
    private ReservationUseCase reservationUseCase;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AmountRepository amountRepository;
    @Autowired
    private ConcertRepository concertRepository;
    @Autowired
    private SeatRepository seatRepository;
    @Autowired
    private ConcertDateRepository concertDateRepository;
    @Autowired
    private WaitingQueueRepository waitingQueueRepository;

    @BeforeEach
    void setUp() throws Exception {
        User user = User.builder()
                .userId(1L).username("홍세영")
                .build();
        userRepository.saveUser(user);
        WaitingQueue queue1 = WaitingQueue.builder()
                .queueId(2L)
                .userId(1L)
                .sequence(1L)
                .status(String.valueOf(WaitingQueueStatus.PROCESS))
                .build();
        waitingQueueRepository.generateQueue(queue1);
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
        seatRepository.saveSeat(seat);
    }

    @Nested
    @DisplayName("예약 결제 확정")
    class confirmReservation {
        
        @Test
        void 결제_성공() {
            //given
            Long userId = 1L;
            User user = userRepository.getUser(userId);
            Concert concert = concertRepository.getConcertById(1L);
            ConcertDate concertDate = concertDateRepository.getConcertDate(1L);

            //when
            ConfirmReservationCommand result = reservationUseCase.confirmReservation(userId, concert.getConcertId(), concertDate.getConcertDateId(), 1, List.of(1L), 1000);

            //then
            assertNotNull(result);
            assertEquals(user.getUsername(), result.username());
            assertEquals(concert.getConcertName(), result.concertName());
        }
        
        @Test
        @DisplayName("결제 동시성 테스트")
        void 세사람이_동시에_한_좌석_결제_시_한_사람만_성공() throws InterruptedException {
            //given

            User user2 = User.builder()
                    .userId(2L).username("홍길동")
                    .build();
            User user3 = User.builder()
                    .userId(3L).username("홍길동")
                    .build();

            userRepository.saveUser(user2);
            userRepository.saveUser(user3);

            int threadCount = 3;
            ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
            CountDownLatch latch = new CountDownLatch(threadCount);

            AtomicInteger failureCount = new AtomicInteger(0);
            AtomicInteger successCount = new AtomicInteger(0);

            List<Long> seatList = List.of(1L);
            List<Long> userIdList = List.of(1L, 2L, 3L);

            for (int i = 0; i < 3; i++) {
                final int index = i;  // index 값을 람다 내부에서 캡처하기 위해 final 선언
                executorService.submit(() -> {
                    try {
                        latch.await(); // 모든 스레드가 동시에 시작되도록 대기
                        Long userId = userIdList.get(index);  // 각 스레드가 다른 사용자로 예약 시도
                        reservationUseCase.confirmReservation(userId, 1L, 1L, 1, seatList, 10000); // 예약 확정 시도
                        successCount.incrementAndGet();
                    } catch (Exception e) {
                        failureCount.incrementAndGet();
                        System.out.println("Reservation failed: " + e.getMessage());
                    }
                });
                latch.countDown(); // 모든 스레드가 시작을 기다림
            }

            executorService.shutdown();
            executorService.awaitTermination(1, TimeUnit.MINUTES);

            Seat seat = seatRepository.getSeat(1L);
            assertEquals(seat.getStatus(), String.valueOf(SeatStatus.CONFIRMED)); // 예약 성공

            assertEquals(1, successCount.get()); // 예약 성공한 스레드는 1개
            assertEquals(2, failureCount.get()); // 예약 실패한 스레드는 2개
        }

    }
}