package com.hhplus.io.app.event.domain.entity;

import com.hhplus.io.common.support.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@Entity
@Table(name = "outbox")
@NoArgsConstructor
@AllArgsConstructor
public class Outbox extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "outbox_id")
    private Long outboxId;

    @Column(name = "aggregate_type")
    private String aggregateType;

    @Column(name = "aggregate_id")
    private String aggregateId;

    @Column(name = "event_type")
    private String eventType;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @Column(name = "is_published", columnDefinition = "TINYINT")
    @Builder.Default()
    private boolean isPublished = false;

    public static Outbox create(AggregateType aggregateType, String key, String message) {
        return Outbox.builder()
                .aggregateType(aggregateType.toString())
                .aggregateId(key)
                .message(message)
                .isPublished(false)
                .build();
    }

    public Outbox published() {
        this.isPublished = true;
        return this;
    }

    public Outbox init() {
        this.isPublished = false;
        return this;
    }


}
