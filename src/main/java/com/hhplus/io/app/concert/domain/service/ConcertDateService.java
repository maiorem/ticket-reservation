package com.hhplus.io.app.concert.domain.service;

import com.hhplus.io.app.concert.domain.ConcertDateInfo;
import com.hhplus.io.app.concert.domain.entity.ConcertDateStatus;
import com.hhplus.io.app.concert.domain.entity.ConcertDate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConcertDateService {

    private final ConcertDateInfo concertDateInfo;

    public ConcertDateService(ConcertDateInfo concertDateInfo) {
        this.concertDateInfo = concertDateInfo;
    }

    public List<ConcertDate> getAllDateListByConcert(Long concertId, ConcertDateStatus status) {
        return concertDateInfo.getAllDateListByConcert(concertId, status);
    }

    public ConcertDate getConcertDate(Long concertDateId) {
        return concertDateInfo.getConcerDate(concertDateId);
    }

    // TODO 오타고쳐주세요!
    public int updateAvailableSeats(Long concertDateId, int confirmSeats) {
        ConcertDate concerDate = concertDateInfo.getConcerDate(concertDateId);
        return concerDate.updateAvailableSeats(confirmSeats);
    }
}
