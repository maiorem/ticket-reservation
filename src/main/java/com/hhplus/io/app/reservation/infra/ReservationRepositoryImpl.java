package com.hhplus.io.app.reservation.infra;

import com.hhplus.io.app.reservation.domain.entity.Reservation;
import com.hhplus.io.app.reservation.domain.repository.ReservationRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

@Repository
public class ReservationRepositoryImpl implements ReservationRepository {

    private final ReservationJpaRepository reservationJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;

    public ReservationRepositoryImpl(ReservationJpaRepository reservationJpaRepository, JPAQueryFactory jpaQueryFactory) {
        this.reservationJpaRepository = reservationJpaRepository;
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Reservation save(Reservation builder) {
        return reservationJpaRepository.save(builder);
    }
}
