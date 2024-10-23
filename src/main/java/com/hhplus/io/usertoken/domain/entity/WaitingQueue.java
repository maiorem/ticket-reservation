package com.hhplus.io.usertoken.domain.entity;


import com.hhplus.io.support.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Builder
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "waiting_queue")
public class WaitingQueue extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "queue_id")
    private Long queueId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "sequence")
    @Comment("대기 순서")
    private Long sequence;

    @Column(name = "status")
    @Comment("대기열 상태 (WAITING, PROCESS, FINISHED, CANCEL)")
    private String status;

    public void udpateStatus(String status) {
        this.status = status;
    }

    public void updateSequence(Long updateSequence) {
        this.sequence = this.sequence - updateSequence;
    }

}
