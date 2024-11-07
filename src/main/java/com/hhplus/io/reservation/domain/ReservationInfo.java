package com.hhplus.io.reservation.domain;

import com.hhplus.io.redis.domain.UserRedisStore;
import com.hhplus.io.redis.domain.repository.RedisRepository;
import com.hhplus.io.reservation.domain.dto.ReservationInfoDTO;
import com.hhplus.io.reservation.domain.entity.Reservation;
import com.hhplus.io.reservation.domain.repository.ReservationRepository;
import com.hhplus.io.support.domain.error.CoreException;
import com.hhplus.io.support.domain.error.ErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ReservationInfo {

    private final ReservationRepository reservationRepository;
    private final RedisRepository redisRepository;

    public ReservationInfo(ReservationRepository reservationRepository, RedisRepository redisRepository) {
        this.reservationRepository = reservationRepository;
        this.redisRepository = redisRepository;
    }

    public ReservationInfoDTO confirmReservation(Long userId) {
        String values = redisRepository.getValues("reserveSeat:" + userId);
        if (values == null) {
            throw new CoreException(ErrorType.EXPIRE_TEMP_RESERVATION);
        }
        log.info("reservation info : {}", values);
        UserRedisStore store = UserRedisStore.converFromString(values);

        Reservation builder = Reservation.builder()
                .userId(userId)
                .concertId(store.concertId())
                .concertDateId(store.concertDateId())
                .build();
        Reservation savedReserve = reservationRepository.save(builder);

        return ReservationInfoDTO.of(store.token(), store.userId(), store.concertId(), store.concertDateId(), savedReserve.getReservationId(), store.seatList());
    }
}
