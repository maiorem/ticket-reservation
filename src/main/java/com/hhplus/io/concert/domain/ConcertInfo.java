package com.hhplus.io.concert.domain;

import com.hhplus.io.concert.domain.entity.Concert;
import com.hhplus.io.concert.persistence.ConcertRepository;
import org.springframework.stereotype.Component;

@Component
public class ConcertInfo {

    private final ConcertRepository concertRepository;

    public ConcertInfo(ConcertRepository concertRepository) {
        this.concertRepository = concertRepository;
    }


    public Concert getConcert(Long concertId) {
        return concertRepository.getConcertById(concertId);
    }
}
