package com.hhplus.io.concert.domain;

import org.springframework.stereotype.Repository;

@Repository
public interface ConcertRepository {
    Concert getConcertById(Long concertId);

    Concert save(Concert concert);
}
