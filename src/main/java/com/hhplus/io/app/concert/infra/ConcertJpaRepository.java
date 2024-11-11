package com.hhplus.io.app.concert.infra;

import com.hhplus.io.app.concert.domain.entity.Concert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConcertJpaRepository extends JpaRepository<Concert, Long> {
    Optional<Concert> findByConcertId(Long concertId);
}
