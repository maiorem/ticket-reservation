package com.hhplus.io.app.event.domain.service;

import com.hhplus.io.app.event.domain.entity.Outbox;
import com.hhplus.io.app.event.domain.repository.OutboxRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OutboxService {

    private final OutboxRepository outboxRepository;

    public OutboxService(OutboxRepository outboxRepository) {
        this.outboxRepository = outboxRepository;
    }

    public Outbox save(Outbox outbox) {
        return outboxRepository.save(outbox);
    }

    public Outbox published(Outbox outbox) {
        return outbox.published();
    }

    public Outbox init(Outbox outbox) {
        return outbox.init();
    }

    public List<Outbox> findPendingEvents() {
        return outboxRepository.findPendingEvents();
    }

    public Outbox findByKey(String key) {
        return outboxRepository.findByAggregateId(key);
    }
}
