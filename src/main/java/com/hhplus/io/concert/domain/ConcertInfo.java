package com.hhplus.io.concert.domain;

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
