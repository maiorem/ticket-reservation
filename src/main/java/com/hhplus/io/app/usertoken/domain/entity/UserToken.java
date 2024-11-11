package com.hhplus.io.app.usertoken.domain.entity;

import com.hhplus.io.common.support.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usertoken")
public class UserToken extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usertoken_id")
    private Long tokenId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "token")
    private String token;

    @Column(name = "is_active", columnDefinition = "TINYINT", length = 1)
    private boolean isActive;

    @Column(name = "token_expire_at")
    private LocalDateTime tokenExpire;

    public void updateIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public void deleteToken() {
        this.token = null;
        this.isActive = false;
        this.tokenExpire = null;
    }

}
