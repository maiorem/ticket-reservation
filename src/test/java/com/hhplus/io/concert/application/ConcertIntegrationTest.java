package com.hhplus.io.concert.application;

import com.hhplus.io.AcceptanceTest;
import com.hhplus.io.concert.domain.entity.SeatStatus;
import com.hhplus.io.concert.domain.entity.Seat;
import com.hhplus.io.concert.persistence.SeatJpaRepository;
import com.hhplus.io.support.domain.error.CoreException;
import com.hhplus.io.support.domain.error.ErrorType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
class ConcertIntegrationTest extends AcceptanceTest {

    @Autowired
    private ConcertUseCase concertUseCase;
    @Autowired
    private SeatJpaRepository seatRepository;

    @Nested
    @DisplayName("좌석 임시 예약")
    class tempReserveSeat {

        @Test
        void 좌석_예약_성공() {
            //given
            Seat seat1 = Seat.builder()
                    .concertId(1L)
                    .concertDateId(1L)
                    .seatNumber("01")
                    .status(String.valueOf(SeatStatus.AVAILABLE))
                    .reservationTime(LocalDateTime.now())
                    .build();
            Seat seat2 = Seat.builder()
                    .concertId(1L)
                    .concertDateId(1L)
                    .seatNumber("02")
                    .status(String.valueOf(SeatStatus.AVAILABLE))
                    .reservationTime(LocalDateTime.now())
                    .build();
            Seat seat3 = Seat.builder()
                    .concertId(1L)
                    .concertDateId(1L)
                    .seatNumber("03")
                    .status(String.valueOf(SeatStatus.AVAILABLE))
                    .reservationTime(LocalDateTime.now())
                    .build();

            seatRepository.save(seat1);
            seatRepository.save(seat2);
            seatRepository.save(seat3);


            List<Long> seatList = List.of(1L, 2L, 3L);

            //when
            SeatReserveCommand result = concertUseCase.tempReserveSeat(seatList);

            //then
            seatRepository.findBySeatId(1L).ifPresent(seat -> {
                assertEquals(3, result.seatList().size(), "전체 예약 성공 건수는 3임");
                assertEquals(seat.getSeatNumber(), result.seatList().get(0).seatNumber(), "첫번째 예약 좌석번호와 1번 좌석번호는 같음");
                assertEquals(SeatStatus.TEMP_RESERVED, result.seatList().get(0).status(), "현재 조회한 좌석의 상태는 TEMP_RESERVED임");
            });

        }

        @Test
        void AVAILABLE상태가_아닌_좌석은_선택할_수_없음() {
            //given
            Seat seat1 = Seat.builder()
                    .concertId(1L)
                    .concertDateId(1L)
                    .seatNumber("04")
                    .status(String.valueOf(SeatStatus.TEMP_RESERVED))
                    .reservationTime(LocalDateTime.now())
                    .build();

            Seat saved = seatRepository.save(seat1);
            Long seatId = saved.getSeatId();

            //when
            CoreException exception = assertThrows(CoreException.class, () -> concertUseCase.tempReserveSeat(List.of(seatId)));

            //then
            assertEquals(ErrorType.FORBIDDEN, exception.getErrorType());
        }

        @Test
        @DisplayName("[낙관적 락] 좌석예약 동시성 테스트")
        void 한_좌석을_여러명의_사용자가_선택하면_한_사람만_성공_해야함() throws InterruptedException {
            //given
            Seat seat1 = Seat.builder()
                    .concertId(1L)
                    .concertDateId(1L)
                    .seatNumber("05")
                    .status(String.valueOf(SeatStatus.AVAILABLE))
                    .reservationTime(LocalDateTime.now())
                    .build();

            Seat saved = seatRepository.save(seat1);
            Long seatId = saved.getSeatId();

            long startTime = System.nanoTime();
            int threadCount = 10;

            List<Long> seatIdList = List.of(seatId);

            ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
            CountDownLatch latch = new CountDownLatch(threadCount);

            AtomicInteger successCount = new AtomicInteger(0);
            AtomicInteger failureCount = new AtomicInteger(0);

            for (int i = 0; i < threadCount; i++) {
                executorService.submit(() -> {
                    try {
                        latch.await();
                        SeatReserveCommand result = concertUseCase.tempReserveSeat(seatIdList);
                        successCount.incrementAndGet();
                        System.out.println("success result : " + result.toString());
                    } catch (Exception e) {
                        System.out.println("Reservation failed: " + e.getMessage());
                        failureCount.incrementAndGet();
                    }
                });
                latch.countDown();
            }

            executorService.shutdown();
            executorService.awaitTermination(30, TimeUnit.SECONDS);

            assertEquals(1, successCount.get(), "한 명만 성공해야 한다.");
            assertEquals(threadCount - successCount.get(), failureCount.get(), "나머지는 실패해야 한다.");

            Seat resultSeat = seatRepository.findBySeatId(seatId).orElseThrow();
            System.out.println("seat status : " + resultSeat.getStatus());
            assertEquals(SeatStatus.TEMP_RESERVED.toString(), resultSeat.getStatus(), "좌석 상태는 TEMP_RESERVED이어야 합니다.");
            long endTime = System.nanoTime();
            long duration = endTime - startTime;

            System.out.println("실행시간 : " + duration + "나노초");

        }

    }

}