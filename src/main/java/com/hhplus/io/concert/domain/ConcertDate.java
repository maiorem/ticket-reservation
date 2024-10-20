package com.hhplus.io.concert.domain;


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
@Table(name = "concert_date")
public class ConcertDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "concert_date_id")
    private Long concertDateId;

    @Column(name = "concert_id")
    private Long concertId;

    @Column(name = "concert_date")
    private LocalDateTime concertDate;

    @Column(name = "status")
    @Comment("콘서트 날짜 상태(AVAILABLE(예약가능), FILLED(만석)")
    private String status;

    @Column(name = "available_seats")
    @Comment("해당 날짜에 현재 이용 가능한 좌석 수")
    private int availableSeats;

    public void initSeat(Concert concert){
        this.availableSeats = concert.getTotalSeats();
    }

    public int updatAvailableSeats(int reservedSeats){
        this.availableSeats = this.availableSeats - reservedSeats;
        return this.availableSeats;
    }

    public void updateStatus(String status){
        this.status = status;
    }

}
