package com.hhplus.io.app.concert.domain.service;

import com.hhplus.io.app.concert.domain.entity.Concert;
import com.hhplus.io.app.concert.domain.repository.ConcertRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class ConcertService {

    private final ConcertRepository concertRepository;

    public ConcertService(ConcertRepository concertRepository) {
        this.concertRepository = concertRepository;
    }


    public Concert getConcert(Long concertId) {
        return concertRepository.getConcertById(concertId);
    }

    public Page<Concert> getConcertList(Pageable pageable) {
        return concertRepository.getConcertList(pageable);
    }

    @Cacheable(cacheNames = "concertList", value = "concertList", key = "'concertList' + ':' + #pageable.pageNumber", cacheManager = "contentCacheManager")
    public Page<Concert> getConcertListWithCache(Pageable pageable) {
        return concertRepository.getConcertList(pageable);
    }

}
