package com.hhplus.io.app.outbox.infra;

import com.hhplus.io.app.outbox.domain.entity.Outbox;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutboxJpaRepository extends JpaRepository<Outbox, Long> {
    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT o FROM outbox o WHERE o.status = false ORDER BY o.createdAt ASC")
    List<Outbox> findPendingEvents();
}
