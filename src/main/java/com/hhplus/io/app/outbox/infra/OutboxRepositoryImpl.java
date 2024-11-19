package com.hhplus.io.app.outbox.infra;

import com.hhplus.io.app.outbox.domain.entity.Outbox;
import com.hhplus.io.app.outbox.domain.repository.OutboxRepository;
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
        return jpaRepository.findPendingEvents();
    }

    @Override
    public Outbox save(Outbox event) {
        return jpaRepository.save(event);
    }
}
