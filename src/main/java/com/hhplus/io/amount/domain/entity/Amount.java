package com.hhplus.io.amount.domain.entity;

import com.hhplus.io.support.domain.BaseEntity;
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

    @Version
    @Column(name = "version")
    @Comment("버전")
    private int version;

    public void setVersion(int version) {
        this.version = version;
    }

    public void saveAmount(int updateAmount){
        this.amount += updateAmount;
    }

    public void payAmount(int payAmount){
        this.amount -= payAmount;
    }

}
