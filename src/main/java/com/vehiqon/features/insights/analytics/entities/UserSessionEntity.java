package com.vehiqon.features.insights.analytics.entities;

import com.vehiqon.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name="user_sessions")
public class UserSessionEntity  {

    @Id
    private UUID id;

//    @Transient
//    private boolean isNew = true;
//
//    @Override
//    public UUID getId() {
//        return id;
//    }
//
//    @Override
//    public boolean isNew() {
//        return isNew;
//    }
//
//    @PostLoad
//    @PostPersist
//    void markNotNew() {
//        this.isNew = false;
//    }

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
    private String deviceName;
    private String appVersion;

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;

    @Builder.Default
    private Long durationSeconds =0L;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private  LocalDateTime updatedAt;
}
