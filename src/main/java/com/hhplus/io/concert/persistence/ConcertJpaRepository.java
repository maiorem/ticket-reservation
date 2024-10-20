package com.hhplus.io.concert.persistence;

import com.hhplus.io.concert.domain.Concert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConcertJpaRepository extends JpaRepository<Concert, Long> {
    Optional<Concert> findByConcertId(Long concertId);
}
