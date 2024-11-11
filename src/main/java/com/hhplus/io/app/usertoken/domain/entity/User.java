package com.hhplus.io.app.usertoken.domain.entity;

import com.hhplus.io.common.support.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_name")
    private String username;

}
