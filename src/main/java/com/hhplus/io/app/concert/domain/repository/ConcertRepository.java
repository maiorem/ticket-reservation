package com.hhplus.io.app.concert.domain.repository;

import com.hhplus.io.app.concert.domain.entity.Concert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ConcertRepository {
    Concert getConcertById(Long concertId);

    Concert save(Concert concert);

    Page<Concert> getConcertList(Pageable pageable, String searchKey);

    Page<Concert> getConcertListWithCache(Pageable pageable);
}
