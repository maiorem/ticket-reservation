package com.hhplus.io.app.concert.domain.service;

import com.hhplus.io.app.concert.domain.ConcertInfo;
import com.hhplus.io.app.concert.domain.entity.Concert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<Concert> getConcertList(Pageable pageable) {
        return concertInfo.getConcertListWithCache(pageable);
    }
}
