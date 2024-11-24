package com.hhplus.io.app.event.infra;

import com.hhplus.io.app.event.domain.entity.Outbox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OutboxJpaRepository extends JpaRepository<Outbox, Long> {

    List<Outbox> findAllByIsPublished(boolean isPublished);

    Optional<Outbox> findByEventKey(String key);
}
