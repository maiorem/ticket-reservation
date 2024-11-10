package com.hhplus.io.concert.domain.entity;


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
@Table(name = "seat")
public class Seat extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id")
    private Long seatId;

    @Column(name = "concert_id")
    private Long concertId;

    @Column(name = "concert_date_id")
    private Long concertDateId;

    @Column(name = "seat_number")
    private String seatNumber;

    @Column(name = "status")
    @Comment("좌석 예약 상태 (TEMP_RESERVED(임시예약), EMPTY(빈 좌석), CONFIRMED(예약확정)")
    private String status;

    @Column(name = "ticket_price")
    @Comment("좌석별 티켓 가격")
    private int ticketPrice;

    @Column(name = "version")
    @Comment("낙관적 락 버전")
    private int version;

    public void setVersion(int version) {
        this.version = version;
    }

    public void updateSeatStatus(String status) {
        this.status = status;
    }

}
