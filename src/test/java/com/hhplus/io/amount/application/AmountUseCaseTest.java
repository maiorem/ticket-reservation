package com.hhplus.io.amount.application;

import com.hhplus.io.DatabaseCleanUp;
import com.hhplus.io.amount.domain.entity.Amount;
import com.hhplus.io.amount.persistence.AmountJpaRepository;
import com.hhplus.io.support.domain.error.CoreException;
import com.hhplus.io.support.domain.error.ErrorType;
import com.hhplus.io.usertoken.domain.entity.User;
import com.hhplus.io.usertoken.persistence.UserJpaRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class AmountUseCaseTest {

    @Autowired
    private AmountUseCase amountUseCase;
    @Autowired
    private AmountJpaRepository amountRepository;
    @Autowired
    private UserJpaRepository userRepository;
    @Autowired
    private DatabaseCleanUp databaseCleanUp;

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
        @DisplayName("잔액 충전 동시성 테스트")
        void 동일_유저가_잔액충전을_동시에_실행할_경우() throws InterruptedException, ExecutionException {
            //given
            Long userId = 1L;
            ExecutorService executor = Executors.newFixedThreadPool(10); // 10개의 스레드로 테스트

            //when
            List<Callable<SaveAmountCommand>> tasks = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                int updateAmount = i * 100; // 각 스레드에서 다른 금액으로 충전
                tasks.add(() -> amountUseCase.saveAmount(userId, updateAmount));
            }

            List<Future<SaveAmountCommand>> futures = executor.invokeAll(tasks);
            executor.shutdown();

            //then
            int totalAmount = 0;
            for (Future<SaveAmountCommand> future : futures) {
                SaveAmountCommand result = future.get();
                assertNotNull(result);
                totalAmount += result.amount();
            }
            assertEquals(100+200+300+400+500+600+700+800+900, totalAmount);
        }

    }

    @AfterEach
    void cleanUp(){
        databaseCleanUp.execute();
    }
}