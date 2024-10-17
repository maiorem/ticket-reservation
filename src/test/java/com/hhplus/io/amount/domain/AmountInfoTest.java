package com.hhplus.io.amount.domain;

import com.hhplus.io.amount.domain.entity.Amount;
import com.hhplus.io.amount.persistence.AmountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AmountInfoTest {

    @Mock
    private AmountRepository amountRepository;
    @InjectMocks
    private AmountInfo amountInfo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        amountRepository = mock(AmountRepository.class);
        amountInfo = new AmountInfo(amountRepository);
    }

    @Test
    @DisplayName("사용자 잔액 조회")
    void getAmountByUser() {
        //given
        Long userId = 1L;
        Amount amount = Amount.builder().amountId(1L).userId(userId).amount(10000).build();
        when(amountRepository.getAmountByUser(userId)).thenReturn(amount);

        //when
        Amount result = amountInfo.getAmountByUser(1L);

        //then
        assertEquals(10000, result.getAmount());
    }

    @Test
    @DisplayName("사용자 잔액 충전")
    void updateAmount() {
        //given
        Long userId = 1L;
        Amount amount = Amount.builder().amountId(1L).userId(userId).amount(10000).build();
        when(amountRepository.getAmountByUser(userId)).thenReturn(amount);

        //when
        int result = amountInfo.updateAmount(userId, 10000);

        //then
        assertEquals(20000, result);
    }

    @Test
    @DisplayName("결제하고 남은 금액 업데이트")
    public void payAmount() {
        //given
        Long userId = 1L;
        Amount amount = Amount.builder().amountId(1L).userId(userId).amount(20000).build();
        when(amountRepository.getAmountByUser(userId)).thenReturn(amount);

        //when
        amountInfo.payAmount(userId, 5000);

        //then
        assertEquals(15000, amount.getAmount());

    }
}