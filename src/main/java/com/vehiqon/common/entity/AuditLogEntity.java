package com.vehiqon.common.entity;

import com.vehiqon.features.onboarding.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name="audit_logs")
public class AuditLogEntity extends BaseEntity {
    @Column(nullable = false)
    private String action;

    @Column(nullable = false)
    private String entity;

    private String userAgent;
    private String description;

    private String status;

    @Column(name = "entity_id")
    private UUID entityId;

    private String ipAddress;

    @Column(name = "user_id")
    private UUID userId;
}
