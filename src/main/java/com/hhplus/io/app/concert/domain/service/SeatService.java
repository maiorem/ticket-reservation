package com.hhplus.io.app.concert.domain.service;

import com.hhplus.io.app.concert.domain.entity.Seat;
import com.hhplus.io.app.concert.domain.entity.SeatStatus;
import com.hhplus.io.app.concert.domain.repository.SeatRepository;
import com.hhplus.io.common.cache.domain.UserRedisStore;
import com.hhplus.io.common.cache.domain.repository.CacheRepository;
import com.hhplus.io.common.support.domain.error.CoreException;
import com.hhplus.io.common.support.domain.error.ErrorType;
import org.springframework.stereotype.Component;

import javax.cache.expiry.Duration;
import java.util.List;

import static com.hhplus.io.common.constants.Constants.CacheText.RESERVE_SEAT_KEY_PREFIX;

@Component
public class SeatService {

    private final SeatRepository seatRepository;
    private final CacheRepository cacheRepository;

    public SeatService(SeatRepository seatRepository, CacheRepository cacheRepository) {
        this.seatRepository = seatRepository;
        this.cacheRepository = cacheRepository;
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
        String values = cacheRepository.getValues(RESERVE_SEAT_KEY_PREFIX + userId);
        if (values != null) {
            cacheRepository.initCache(RESERVE_SEAT_KEY_PREFIX + userId);
        }
        cacheRepository.setValues(RESERVE_SEAT_KEY_PREFIX + userId, String.valueOf(UserRedisStore.of(token, userId, concertId, concertDateId, seatList)), Duration.FIVE_MINUTES);
    }
}

