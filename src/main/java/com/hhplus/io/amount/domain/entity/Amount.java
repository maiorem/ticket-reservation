package com.hhplus.io.amount.domain.entity;

import com.hhplus.io.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Builder
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "amount")
public class Amount extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "amount_id")
    private Long amountId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "amount")
    @Comment("사용자 잔액")
    private int amount;

    @Column(name = "payment_time")
    @Comment("결제에 사용된 시간")
    private LocalDateTime paymentTime;

    public void saveAmount(int updateAmount){
        this.amount += updateAmount;
    }

    public void payAmount(int payAmount){
        this.amount -= payAmount;
    }

    public void updatePaymentDate(LocalDateTime updateTime){
        this.paymentTime = updateTime;
    }


}
