package com.hhplus.io.app.reservation.domain;

import com.hhplus.io.common.redis.domain.repository.CacheRepository;
import com.hhplus.io.app.reservation.domain.entity.ReservationSeat;
import com.hhplus.io.app.reservation.domain.repository.ReservationSeatRepository;
import com.hhplus.io.common.support.domain.error.CoreException;
import com.hhplus.io.common.support.domain.error.ErrorType;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class ReservationSeatInfo {

    private final ReservationSeatRepository reservationSeatRepository;
    private final CacheRepository cacheRepository;

    public ReservationSeatInfo(ReservationSeatRepository reservationSeatRepository, CacheRepository cacheRepository) {
        this.reservationSeatRepository = reservationSeatRepository;
        this.cacheRepository = cacheRepository;
    }


    public ReservationSeat saveSeat(Long userId, Long reservationId, Long seatId) {
        ReservationSeat builder = ReservationSeat.builder()
                .userId(userId)
                .reservationId(reservationId)
                .seatId(seatId).build();
        ReservationSeat savedReservation = reservationSeatRepository.save(builder);

        String values = cacheRepository.getValues("reserveSeat:" + userId);
        if (values == null || StringUtils.isEmpty(values)) {
            throw new CoreException(ErrorType.FORBIDDEN);
        }

        return savedReservation;

    }
}
