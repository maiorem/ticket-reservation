package com.hhplus.io.usertoken.persistence;

import com.hhplus.io.usertoken.domain.entity.WaitingQueue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WaitingQueueJpaRepository extends JpaRepository<WaitingQueue, Long> {
    Optional<WaitingQueue> findByUserId(Long userId);

    Optional<WaitingQueue> findByUserIdAndStatus(Long userId, String status);

    List<WaitingQueue> findAllByStatus(String status);

    Long countByStatus(String status);
}
