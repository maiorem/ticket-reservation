package com.hhplus.io.amount.application;

import com.hhplus.io.AcceptanceTest;
import com.hhplus.io.amount.domain.entity.Amount;
import com.hhplus.io.amount.persistence.AmountJpaRepository;
import com.hhplus.io.amount.service.AmountService;
import com.hhplus.io.support.domain.error.CoreException;
import com.hhplus.io.support.domain.error.ErrorType;
import com.hhplus.io.usertoken.domain.entity.User;
import com.hhplus.io.usertoken.persistence.UserJpaRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
class AmountIntegrationTest extends AcceptanceTest {

    @Autowired
    private AmountUseCase amountUseCase;
    @Autowired
    private AmountService amountService;
    @Autowired
    private AmountJpaRepository amountRepository;
    @Autowired
    private UserJpaRepository userRepository;

    @BeforeEach
    void setUp() {
        Long userId = 1L;
        User user1 = User.builder()
                .userId(userId).username("예이츠")
                .build();
        userRepository.save(user1);

        Amount amount1 = Amount.builder()
                .amountId(1L)
                .userId(1L)
                .amount(10000)
                .build();
        amountRepository.save(amount1);
    }

    @Nested
    @DisplayName("잔액 조회")
    class GetAmountByUser {
        @Test
        void 잔액_조회_성공() {
            //given
            Long userId = 1L;

            //when
            int amount = amountUseCase.getAmountByUser(userId);

            //then
            assertEquals(10000, amount);
        }

        @Test
        void 존재하지_않는_사용자는_잔액_조회_불가능함(){
            //given
            Long userId = 4L;

            //when
            CoreException exception = assertThrows(CoreException.class, () -> amountUseCase.getAmountByUser(userId));

            //then
            assertEquals(ErrorType.USER_NOT_FOUND, exception.getErrorType());
        }


    }

    @Nested
    @DisplayName("잔액 충전")
    class SaveAmount {

        @Test
        void 잔액_충전_성공(){
            //given
            Long userId = 1L;

            //when
            SaveAmountCommand command = amountUseCase.saveAmount(userId, 10000);

            //then
            assertNotNull(command);
            assertEquals(10000+10000, command.amount());
        }

        @Test
        void 음수로는_충전할_수_없음() {
            //given
            Long userId = 1L;

            //when
            CoreException exception = assertThrows(CoreException.class, () -> amountUseCase.saveAmount(userId, -1));

            //then
            assertEquals(ErrorType.FORBIDDEN, exception.getErrorType());
            assertEquals("해당 값을 사용할 수 없습니다.", exception.getMessage());
        }

        @Test
        @Rollback(value = false)
        @DisplayName("[비관적 락] 잔액 충전 동시성 테스트")
        void 동일_유저가_잔액충전을_동시에_실행할_경우() throws InterruptedException {
            //given
            long startTime = System.nanoTime();
            Long userId = 1L;
            int updateAmount = 100;
            int threadCount = 10;

            ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
            CountDownLatch latch = new CountDownLatch(threadCount);
            List<SaveAmountCommand> commands = new ArrayList<>();

            for (int i = 0; i < threadCount; i++) {
                executorService.submit(() -> {
                    try {
                        SaveAmountCommand command = amountUseCase.saveAmount(userId, updateAmount);
                        commands.add(command);
                    } finally {
                        latch.countDown();
                    }
                });
            }
            latch.await();
            executorService.shutdown();

            assertNotNull(commands.get(0));
            assertEquals(10000+updateAmount, commands.get(0).amount(), "잔액이 한번만 충전되어야 함");

            long endTime = System.nanoTime();
            long duration = endTime - startTime;

            System.out.println("실행시간 : " + duration + " 나노초");

        }

    }

    @Nested
    @DisplayName("잔액 사용")
    class payAmount {

        @Test
        void 잔액_사용_성공() {
            //given
            Long userId = 1L;
            int payAmount = 10000;

            //when
            amountService.pay(userId, payAmount);

            //then
            Amount amount = amountRepository.findByUserId(userId).orElseThrow();
            assertEquals(0, amount.getAmount());

        }

        @Test
        void 기존_잔액보다_큰_금액_결제시_실패() {
            //given
            Long userId = 1L;
            int payAmount = 15000;

            //when
            CoreException exception = assertThrows(CoreException.class, () -> amountService.pay(userId, payAmount));

            //then
            assertEquals(ErrorType.FORBIDDEN, exception.getErrorType());

        }

        @Test
        @Rollback(value = false)
        @DisplayName("[비관적 락] 결제 동시성 테스트")
        void 동일_유저가_동시에_여러번_결제를_하는_경우() throws InterruptedException {
            //given
            long startTime = System.nanoTime();
            Long userId = 1L;
            int payAmount = 10000;
            int threadCount = 10;

            ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
            CountDownLatch latch = new CountDownLatch(threadCount);

            for (int i = 0; i < threadCount; i++) {
                executorService.submit(() -> {
                    try {
                        amountService.pay(userId, payAmount);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                });
                latch.countDown();
            }
            latch.await();
            executorService.shutdown();

            Amount amount = amountRepository.findByUserId(userId).orElseThrow();
            assertEquals(0, amount.getAmount(), "잔액 결제는 1회만 되어야 한다.");

            long endTime = System.nanoTime();
            long duration = endTime - startTime;

            System.out.println("실행시간 : " + duration + " 나노초");
        }
    }

}