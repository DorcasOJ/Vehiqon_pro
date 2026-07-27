package com.vehiqon.features.carmgmt.entities;

import com.vehiqon.common.entity.BaseEntity;
import com.vehiqon.features.carmgmt.enums.MaintenanceStatus;
import com.vehiqon.features.carmgmt.enums.MaintenanceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name="maintenance_notification_history")
public class MaintenanceHistoryEntity extends BaseEntity {

    @Column(name = "maintenance_reminder_id", nullable = false)
    private UUID MaintenanceReminderId;

    @Enumerated(EnumType.STRING)
    private MaintenanceStatus status;
    private String provider;
    private String providerMessageId;
    private String errorMessage;
    @Column(nullable = false)
    private LocalDateTime attemptedAt;

}
