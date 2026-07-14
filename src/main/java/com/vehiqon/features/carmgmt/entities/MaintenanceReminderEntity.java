package com.vehiqon.features.carmgmt.entities;


import com.vehiqon.common.entity.BaseEntity;
import com.vehiqon.features.carmgmt.enums.MaintenanceType;
import com.vehiqon.features.carmgmt.enums.MaintenanceStatus;
import com.vehiqon.features.onboarding.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;
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
@Table(name="maintenance_reminders")
public class MaintenanceReminderEntity extends BaseEntity {
    @Column(name = "car_id", nullable = false)
    private UUID carId;

    private String title;

    @Column(length = 1200)
    private String description;

    @Enumerated(EnumType.STRING)
    private MaintenanceType type;

    @Enumerated(EnumType.STRING)
    private MaintenanceStatus status;

    private LocalDate appointmentDate;

    private LocalTime appointmentTime;

    private Integer odometer;

    private BigDecimal estimatedCost;

    private String workshop;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Builder.Default
    private Boolean notificationSent = false;

    private LocalDate notificationDate;

    private LocalDateTime notificationSentAt;
    private LocalDate dueDate;
}
