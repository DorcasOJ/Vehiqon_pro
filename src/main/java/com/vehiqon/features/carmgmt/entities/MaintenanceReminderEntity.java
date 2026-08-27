package com.vehiqon.features.carmgmt.entities;


import com.vehiqon.common.entity.BaseEntity;
import com.vehiqon.common.entity.BaseWithDeleteEntity;
import com.vehiqon.features.carmgmt.enums.NotificationChannelEnum;
import com.vehiqon.features.carmgmt.enums.NotificationStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name="maintenance_reminders")
public class MaintenanceReminderEntity extends BaseWithDeleteEntity {

    @Column(name = "car_maintenance_id", nullable = false)
    private UUID carMaintenanceId;

    private String reminderName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private NotificationStatus notificationStatus = NotificationStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private NotificationChannelEnum notificationChannel = NotificationChannelEnum.EMAIL;

    private Instant scheduledAt;
    private Instant queuedAt;
    private Instant sentAt;
    private Instant failedAt;

    @Column(length = 1000)
    private String failureReason;

    @Column(nullable = false)
    @Builder.Default
    private Integer attemptCount = 0;

}
