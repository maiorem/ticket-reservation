package com.hhplus.io.user.persistence;

import com.hhplus.io.user.domain.WaitingQueue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WaitingQueueJpaRepository extends JpaRepository<WaitingQueue, Long> {
}
