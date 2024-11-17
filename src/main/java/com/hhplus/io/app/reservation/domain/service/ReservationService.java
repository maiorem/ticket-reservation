package com.hhplus.io.app.reservation.domain.service;

import com.hhplus.io.common.cache.domain.UserRedisStore;
import com.hhplus.io.common.cache.domain.repository.CacheRepository;
import com.hhplus.io.app.reservation.domain.dto.ReservationStore;
import com.hhplus.io.app.reservation.domain.entity.Reservation;
import com.hhplus.io.app.reservation.domain.repository.ReservationRepository;
import com.hhplus.io.common.support.domain.error.CoreException;
import com.hhplus.io.common.support.domain.error.ErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.hhplus.io.common.constants.Constants.CacheText.RESERVE_SEAT_KEY_PREFIX;

@Slf4j
@Component
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final CacheRepository cacheRepository;

    public ReservationService(ReservationRepository reservationRepository, CacheRepository cacheRepository) {
        this.reservationRepository = reservationRepository;
        this.cacheRepository = cacheRepository;
    }

    public ReservationStore confirmReservation(Long userId) {
        String values = cacheRepository.getValues(RESERVE_SEAT_KEY_PREFIX + userId);
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

        return ReservationStore.of(store.token(), store.userId(), store.concertId(), store.concertDateId(), savedReserve.getReservationId(), store.seatList());
    }
}
