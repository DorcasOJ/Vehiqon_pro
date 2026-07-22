package com.vehiqon.features.insights.analytics.entities;

import com.vehiqon.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name="user_sessions")
public class UserSessionEntity extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private UUID userId;
    @Column(name = "login_at", nullable = false)
    private LocalDateTime loginAt;
    @Column(name = "logout_at", nullable = false)
    private LocalDateTime logoutAt;
    @Column(name = "last_activity_at", nullable = false)
    private LocalDateTime lastActivityAt;
    private String device;
    private String browser;
    private String platform;
    private String ipAddress;
    private String city;
    private String country;
    private String deviceId; // each device has it own session
    private String appVersion;

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;

    private Long durationSeconds;
}
