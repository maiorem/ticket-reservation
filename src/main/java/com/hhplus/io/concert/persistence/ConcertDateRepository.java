package com.hhplus.io.concert.persistence;

import com.hhplus.io.concert.domain.entity.ConcertDate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConcertDateRepository {
    List<ConcertDate> getAllDateListByConcert(Long concertId, String status);

    ConcertDate getConcertDate(Long concertDateId);

}
