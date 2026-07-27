package com.vehiqon.features.carmgmt.dto;

import com.vehiqon.features.carmgmt.enums.NotificationChannelEnum;
import com.vehiqon.features.carmgmt.enums.NotificationStatus;
import jakarta.validation.constraints.*;

import java.time.Instant;
import java.util.UUID;

public class MaintenanceReminderDto {

    private MaintenanceReminderDto() {}


//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    @Builder.Default
//    private NotificationStatusEnum notificationStatus = NotificationStatusEnum.PENDING;
//
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    @Builder.Default
//    private NotificationChannelEnum notificationChannel = NotificationChannelEnum.EMAIL;
//
//    private Instant scheduledAt;
//    private Instant queuedAt;
//    private Instant sentAt;
//    private Instant failedAt;
//
//    @Column(length = 1000)
//    private String failureReason;
//
//    @Column(nullable = false)
//    @Builder.Default
//    private Integer attemptCount = 0;

    public record CreateMaintenanceReminderRequest(
            @NotNull(message = "Car Maintenance is required")
            UUID carMaintenanceId,
            String reminderName,
            @NotNull
            NotificationStatus notificationStatus,
            @NotNull
            NotificationChannelEnum notificationChannel,

            @Future(message = "scheduledAt date cannot be in the past")
            Instant scheduledAt,
            Instant queuedAt,
            Instant sentAt,
            Instant failedAt,
            String failureReason,
            @PositiveOrZero
            Integer attemptCount

    ) {}

//    public record MaintenanceResponse(
//
//            UUID id,
//            UUID carId,
//            String title,
//            String description,
//            MaintenanceType type,
//            MaintenanceStatus status,
//            LocalDate appointmentDate,
//            LocalTime appointmentTime,
//            LocalDate notificationDate,
//            LocalDateTime notificationSentAt,
//            LocalDate dueDate,
//            Integer odometer,
//            BigDecimal estimatedCost,
//            String workshop,
//            String notes
//
//    ) {}

    public record UpdateMaintenanceRequest(

            UUID carMaintenanceId,
            String reminderName,
            NotificationStatus notificationStatus,
            NotificationChannelEnum notificationChannel,
            Instant scheduledAt,
            Instant queuedAt,
            Instant sentAt,
            Instant failedAt,
            String failureReason,
            Integer attemptCount

    ) {}
}
