package com.hhplus.io.concert.domain;

import com.hhplus.io.concert.domain.entity.ConcertDate;
import com.hhplus.io.concert.domain.entity.ConcertDateStatus;
import com.hhplus.io.concert.domain.repository.ConcertDateRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConcertDateInfo {

    private final ConcertDateRepository concertDateRepository;

    public ConcertDateInfo(ConcertDateRepository concertDateRepository) {
        this.concertDateRepository = concertDateRepository;
    }


    public List<ConcertDate> getAllDateListByConcert(Long concertId, ConcertDateStatus status) {
        return concertDateRepository.getAllDateListByConcert(concertId, String.valueOf(status));
    }

    public ConcertDate getConcerDate(Long concertDateId) {
        return concertDateRepository.getConcertDate(concertDateId);
    }

}
