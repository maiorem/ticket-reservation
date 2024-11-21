package com.hhplus.io.app.event.domain.repository;

import com.hhplus.io.app.event.domain.entity.Outbox;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutboxRepository {

    List<Outbox> findPendingEvents();

    Outbox save(Outbox event);

    Outbox findByEventKey(String key);
}
