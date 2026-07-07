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
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name="maintenance_reminders")
public class MaintenanceReminderEntity extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id", nullable = false)
    private CarEntity carEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private String title;

    @Column(length = 1200)
    private String description;
    @Enumerated(EnumType.STRING)
    private MaintenanceType type;
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    private MaintenanceStatus status;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private Integer odometer;
    private BigDecimal estimatedCost;
    private String workshop;

    private String notes;

    @Builder.Default
    private Boolean notificationSent = false;
    private LocalDate notificationDate;

}
