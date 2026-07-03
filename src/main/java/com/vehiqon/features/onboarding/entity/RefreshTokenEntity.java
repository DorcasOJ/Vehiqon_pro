package com.vehiqon.features.onboarding.entity;

import com.vehiqon.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshTokenEntity extends BaseEntity {
    @Column(nullable = false, unique = true, length = 512)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    /**
     * Chrome on Windows
     * Safari on iPhone
     * Android App
     */
    private String deviceName;

    /**
     * Browser fingerprint/device id
     */
    @Column(length = 255)
    private String deviceId;

    private String ipAddress;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Builder.Default
    private Boolean revoked = false;

    @Builder.Default
    private Boolean expired = false;

    private LocalDateTime revokedAt;
}
