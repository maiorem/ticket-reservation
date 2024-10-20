package com.hhplus.io.concert.persistence;

import com.hhplus.io.concert.domain.ConcertDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConcertDateJpaRepository extends JpaRepository<ConcertDate, Long> {

    List<ConcertDate> findAllByConcertId(Long concertId);

    List<ConcertDate> findAllByConcertIdAndStatus(Long concertId, String status);

    Optional<ConcertDate> findByConcertDateId(Long concertDateId);
}
