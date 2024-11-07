package com.hhplus.io.reservation.domain;

import com.hhplus.io.redis.domain.repository.RedisRepository;
import com.hhplus.io.reservation.domain.entity.ReservationSeat;
import com.hhplus.io.reservation.domain.repository.ReservationSeatRepository;
import com.hhplus.io.support.domain.error.CoreException;
import com.hhplus.io.support.domain.error.ErrorType;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class ReservationSeatInfo {

    private final ReservationSeatRepository reservationSeatRepository;
    private final RedisRepository redisRepository;

    public ReservationSeatInfo(ReservationSeatRepository reservationSeatRepository, RedisRepository redisRepository) {
        this.reservationSeatRepository = reservationSeatRepository;
        this.redisRepository = redisRepository;
    }


    public ReservationSeat saveSeat(Long userId, Long reservationId, Long seatId) {
        ReservationSeat builder = ReservationSeat.builder()
                .userId(userId)
                .reservationId(reservationId)
                .seatId(seatId).build();
        ReservationSeat savedReservation = reservationSeatRepository.save(builder);

        String values = redisRepository.getValues("reserveSeat:" + userId);
        if (values == null || StringUtils.isEmpty(values)) {
            throw new CoreException(ErrorType.FORBIDDEN);
        }

        return savedReservation;

    }
}
