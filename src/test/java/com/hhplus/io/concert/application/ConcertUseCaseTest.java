package com.hhplus.io.concert.application;

import com.hhplus.io.concert.domain.entity.SeatStatus;
import com.hhplus.io.concert.domain.entity.Seat;
import com.hhplus.io.concert.domain.repository.SeatRepository;
import jakarta.transaction.Transactional;
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
@Transactional
class ConcertUseCaseTest {

    @Autowired
    private ConcertUseCase concertUseCase;
    @Autowired
    private SeatRepository seatRepository;


    @Nested
    @DisplayName("좌석 임시 예약")
    class tempReserveSeat {

        @Test
        void 좌석_예약_성공() {
            //given
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
            seatRepository.saveSeat(seat1);
            seatRepository.saveSeat(seat2);
            seatRepository.saveSeat(seat3);

            //when
            SeatReserveCommand result = concertUseCase.tempReserveSeat(List.of(1L, 2L, 3L));

            //then
            assertEquals(3, result.seatList().size());
            assertEquals(seat1.getSeatNumber(), result.seatList().get(0).seatNumber());

            Seat seat = seatRepository.getSeat(1L);
            assertEquals(String.valueOf(SeatStatus.TEMP_RESERVED), seat.getStatus());
        }



        @Test
        void AVAILABLE상태가_아닌_좌석은_선택할_수_없음() {
            //given
            Seat seat1 = Seat.builder()
                    .seatId(1L)
                    .concertId(1L)
                    .concertDateId(1L)
                    .seatNumber("01")
                    .status(String.valueOf(SeatStatus.TEMP_RESERVED))
                    .reservationTime(LocalDateTime.now())
                    .build();
            seatRepository.saveSeat(seat1);

            //when
            RuntimeException exception = assertThrows(RuntimeException.class, () -> concertUseCase.tempReserveSeat(List.of(1L)));

            //then
            assertEquals("seat not available", exception.getMessage());
        }

        @Test
        @DisplayName("좌석예약 동시성 테스트")
        void 한_좌석을_세명의_사용자가_선택하면_한_사람만_성공_해야함() throws InterruptedException {
            Seat seat1 = Seat.builder()
                    .seatId(1L)
                    .concertId(1L)
                    .concertDateId(1L)
                    .seatNumber("01")
                    .status(String.valueOf(SeatStatus.AVAILABLE))
                    .reservationTime(LocalDateTime.now())
                    .build();
            seatRepository.saveSeat(seat1);

            int threadCount = 3;
            ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
            CountDownLatch latch = new CountDownLatch(threadCount);
            AtomicInteger failureCount = new AtomicInteger(0);
            AtomicInteger successCount = new AtomicInteger(0);

            List<Long> seatIdList = List.of(1L); // 동일한 좌석에 대해 동시에 예약

            for (int i = 0; i < 3; i++) {
                executorService.submit(() -> {
                    try {
                        latch.await(); // 모든 스레드가 동시에 시작되도록 대기
                        concertUseCase.tempReserveSeat(seatIdList); // 예약 시도
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
            assertEquals(seat.getStatus(), String.valueOf(SeatStatus.TEMP_RESERVED)); // 예약 성공

            assertEquals(1, successCount.get()); // 예약 성공한 스레드는 1개
            assertEquals(2, failureCount.get()); // 예약 실패한 스레드는 2개

        }



    }


}