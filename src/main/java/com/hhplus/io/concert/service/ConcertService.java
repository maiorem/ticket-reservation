package com.hhplus.io.concert.service;

import com.hhplus.io.concert.domain.ConcertInfo;
import com.hhplus.io.concert.domain.entity.Concert;
import org.springframework.stereotype.Service;

@Service
public class ConcertService {

    private final ConcertInfo concertInfo;

    public ConcertService(ConcertInfo concertInfo) {
        this.concertInfo = concertInfo;
    }

    public Concert getConcert(Long concertId) {
        return concertInfo.getConcert(concertId);
    }
}
