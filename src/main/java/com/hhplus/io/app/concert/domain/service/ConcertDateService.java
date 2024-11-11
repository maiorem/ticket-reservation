package com.hhplus.io.app.concert.domain.service;

import com.hhplus.io.app.concert.domain.entity.ConcertDate;
import com.hhplus.io.app.concert.domain.entity.ConcertDateStatus;
import com.hhplus.io.app.concert.domain.repository.ConcertDateRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConcertDateService {

    private final ConcertDateRepository concertDateRepository;

    public ConcertDateService(ConcertDateRepository concertDateRepository) {
        this.concertDateRepository = concertDateRepository;
    }


    public List<ConcertDate> getAllDateListByConcert(Long concertId, ConcertDateStatus status) {
        return concertDateRepository.getAllDateListByConcert(concertId, String.valueOf(status));
    }

    public ConcertDate getConcertDate(Long concertDateId) {
        return concertDateRepository.getConcertDate(concertDateId);
    }

    public int updateAvailableSeats(Long concertDateId, int size) {
        ConcertDate concertDate = getConcertDate(concertDateId);
        concertDate.updateAvailableSeats(size);
        return concertDate.getAvailableSeats();
    }
}
