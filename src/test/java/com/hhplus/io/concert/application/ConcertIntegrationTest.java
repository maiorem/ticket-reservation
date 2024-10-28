package com.hhplus.io.concert.application;

import com.hhplus.io.DatabaseCleanUp;
import com.hhplus.io.concert.domain.entity.SeatStatus;
import com.hhplus.io.concert.domain.entity.Seat;
import com.hhplus.io.concert.persistence.SeatJpaRepository;
import com.hhplus.io.support.domain.error.CoreException;
import com.hhplus.io.support.domain.error.ErrorType;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class ConcertIntegrationTest {

    @Autowired
    private ConcertUseCase concertUseCase;
    @Autowired
    private SeatJpaRepository seatRepository;
    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @BeforeEach
    void setUp() {
        Seat seat1 = Seat.builder()
                .seatId(1L)
                .concertId(1L)
                .concertDateId(1L)
                .seatNumber("01")
                .status(String.valueOf(SeatStatus.AVAILABLE))
                .reservationTime(LocalDateTime.now())
                .build();
        Seat seat2 = Seat.builder()
                .seatId(2L)
                .concertId(1L)
                .concertDateId(1L)
                .seatNumber("02")
                .status(String.valueOf(SeatStatus.AVAILABLE))
                .reservationTime(LocalDateTime.now())
                .build();
        Seat seat3 = Seat.builder()
                .seatId(3L)
                .concertId(1L)
                .concertDateId(1L)
                .seatNumber("03")
                .status(String.valueOf(SeatStatus.AVAILABLE))
                .reservationTime(LocalDateTime.now())
                .build();
        Seat seat5 = Seat.builder()
                .seatId(5L)
                .concertId(1L)
                .concertDateId(1L)
                .seatNumber("05")
                .status(String.valueOf(SeatStatus.AVAILABLE))
                .reservationTime(LocalDateTime.now())
                .build();
        seatRepository.save(seat1);
        seatRepository.save(seat2);
        seatRepository.save(seat3);
        seatRepository.save(seat5);
    }

    @Nested
    @DisplayName("좌석 임시 예약")
    class tempReserveSeat {

        @Test
        void 좌석_예약_성공() {
            //given
            Long seatId = 1L;

            //when
            SeatReserveCommand result = concertUseCase.tempReserveSeat(List.of(1L, 2L, 3L));

            //then
            seatRepository.findBySeatId(seatId).ifPresent(seat -> {
                assertEquals(3, result.seatList().size());
                assertEquals(seat.getSeatNumber(), result.seatList().get(0).seatNumber());
                assertEquals(String.valueOf(SeatStatus.TEMP_RESERVED), seat.getStatus());
            });

        }

        @Test
        void AVAILABLE상태가_아닌_좌석은_선택할_수_없음() {
            //given
            Seat seat1 = Seat.builder()
                    .seatId(4L)
                    .concertId(1L)
                    .concertDateId(1L)
                    .seatNumber("04")
                    .status(String.valueOf(SeatStatus.TEMP_RESERVED))
                    .reservationTime(LocalDateTime.now())
                    .build();
            seatRepository.save(seat1);

            //when
            CoreException exception = assertThrows(CoreException.class, () -> concertUseCase.tempReserveSeat(List.of(4L)));

            //then
            assertEquals(ErrorType.SEAT_NOT_FOUND, exception.getErrorType());
        }

        @Test
        @DisplayName("좌석예약 동시성 테스트")
        void 한_좌석을_세명의_사용자가_선택하면_한_사람만_성공_해야함() throws InterruptedException {
            int threadCount = 3;
            ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
            CountDownLatch latch = new CountDownLatch(1);

            List<Long> seatIds = new ArrayList<>();
            seatIds.add(5L);

            List<Future<Boolean>> results = new ArrayList<>();

            AtomicInteger failureCount = new AtomicInteger(0);
            AtomicInteger successCount = new AtomicInteger(0);

            // 3개의 스레드로 동시에 좌석 예약을 시도
            for (int i = 0; i < threadCount; i++) {
                Future<Boolean> result = executorService.submit(() -> {
                    try {
                        latch.await(); // 모든 스레드가 동시에 시작할 수 있도록 대기
                        SeatReserveCommand command = concertUseCase.tempReserveSeat(seatIds);
                        successCount.incrementAndGet();
                        return !(command==null); // 예약 성공 시 true 반환
                    } catch (Exception e) {
                        System.out.println("Reservation fail : "+ e.getMessage());
                        failureCount.incrementAndGet();
                        return false; // 예약 실패 시 false 반환
                    }
                });
                results.add(result);
            }

            latch.countDown(); // 모든 스레드 시작

            assertEquals(1, successCount);
            assertEquals(2, failureCount);

            executorService.shutdown();

        }

    }
    @AfterEach
    void cleanUp(){
        databaseCleanUp.execute();
    }


}