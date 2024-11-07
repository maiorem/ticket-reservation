package com.hhplus.io.concert.domain;

import com.hhplus.io.common.constants.Constants;
import com.hhplus.io.concert.domain.entity.Seat;
import com.hhplus.io.concert.domain.entity.SeatStatus;
import com.hhplus.io.concert.domain.repository.SeatRepository;
import com.hhplus.io.redis.domain.UserRedisStore;
import com.hhplus.io.redis.domain.repository.RedisRepository;
import com.hhplus.io.support.domain.error.CoreException;
import com.hhplus.io.support.domain.error.ErrorType;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.cache.expiry.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class SeatInfo {

    private final SeatRepository seatRepository;
    private final RedisRepository redisRepository;

    public SeatInfo(SeatRepository seatRepository, RedisRepository redisRepository) {
        this.seatRepository = seatRepository;
        this.redisRepository = redisRepository;
    }

    public List<Seat> getAllSeatByConcertDate(Long concertDateId, SeatStatus status) {
        return seatRepository.getAllSeatByDate(concertDateId, String.valueOf(status));
    }

    public Seat getSeat(Long seatId) {
        return seatRepository.getSeat(seatId);
    }

    public void updateStatusAndReservationTime(Long seatId, SeatStatus fromStatus, SeatStatus updateStatus) {
        Seat seat = seatRepository.getSeat(seatId);
        if (seat == null) {
            throw new CoreException(ErrorType.SEAT_NOT_FOUND);
        }
        if (!seat.getStatus().equals(fromStatus.toString())) {
            throw new CoreException(ErrorType.FORBIDDEN);
        }
        seat.updateSeatStatus(String.valueOf(updateStatus));
    }

    public void tempReserveSeat(String token, Long userId, Long concertId, Long concertDateId, List<Long> seatList) {
        String values = redisRepository.getValues("reserveSeat:" + userId);
        if (values != null) {
            redisRepository.initCache("reserveSeat:" + userId);
        }
        redisRepository.setValues("reserveSeat:" + userId, String.valueOf(UserRedisStore.of(token, userId, concertId, concertDateId, seatList)), Duration.FIVE_MINUTES);
    }
}

