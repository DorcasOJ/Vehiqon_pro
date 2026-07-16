package com.vehiqon.features.onboarding.entity;


import com.vehiqon.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "password_reset_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordResetTokenEntity extends BaseEntity {

    private String token;
    private UUID userId;
    private Instant expiresAt;
    private boolean used;
}
