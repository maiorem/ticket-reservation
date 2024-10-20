package com.hhplus.io.concert.domain.repository;

import com.hhplus.io.concert.domain.entity.Concert;
import org.springframework.stereotype.Repository;

@Repository
public interface ConcertRepository {
    Concert getConcertById(Long concertId);

    Concert save(Concert concert);
}
