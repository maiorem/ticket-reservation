package com.hhplus.io.concert.domain;

import com.hhplus.io.concert.domain.entity.Seat;
import com.hhplus.io.concert.domain.entity.SeatStatus;
import com.hhplus.io.concert.domain.repository.SeatRepository;
import com.hhplus.io.redis.domain.UserRedisStore;
import com.hhplus.io.redis.domain.repository.RedisRepository;
import com.hhplus.io.support.domain.error.CoreException;
import com.hhplus.io.support.domain.error.ErrorType;
import org.springframework.stereotype.Component;

import javax.cache.expiry.Duration;
import java.util.List;

import static com.hhplus.io.common.constants.Constants.CacheText.RESERVE_SEAT_KEY_PREFIX;

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
        String values = redisRepository.getValues(RESERVE_SEAT_KEY_PREFIX + userId);
        if (values != null) {
            redisRepository.initCache(RESERVE_SEAT_KEY_PREFIX + userId);
        }
        redisRepository.setValues(RESERVE_SEAT_KEY_PREFIX + userId, String.valueOf(UserRedisStore.of(token, userId, concertId, concertDateId, seatList)), Duration.FIVE_MINUTES);
    }
}

