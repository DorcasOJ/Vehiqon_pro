package com.vehiqon.features.insights.analytics.entities;

import com.vehiqon.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name="user_events")
public class UserSessionEntity extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    private LocalDateTime loginTime;
    private LocalDateTime logoutTime;
    private LocalDateTime lastActivityAt;
    private String device;
    private String browser;
    private String platform;
    private String ipAddress;
    private String city;
    private String country;
    private String deviceId; // each device has it own session
    private String appVersion;

    private Long totalDurationSeconds;
}
