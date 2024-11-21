package com.hhplus.io.app.event.infra;

import com.hhplus.io.app.event.domain.entity.Outbox;
import com.hhplus.io.app.event.domain.repository.OutboxRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OutboxRepositoryImpl implements OutboxRepository {

    private final OutboxJpaRepository jpaRepository;

    public OutboxRepositoryImpl(OutboxJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<Outbox> findPendingEvents() {
        return jpaRepository.findAllByIsPublished(false);
    }

    @Override
    public Outbox save(Outbox event) {
        return jpaRepository.save(event);
    }

    @Override
    public Outbox findByEventKey(String key) {
        return jpaRepository.findByEventKey(key).orElse(null);
    }
}
