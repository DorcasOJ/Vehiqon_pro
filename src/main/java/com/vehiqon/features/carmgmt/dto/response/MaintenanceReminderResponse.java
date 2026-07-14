package com.vehiqon.features.carmgmt.dto.response;

import com.vehiqon.features.carmgmt.enums.MaintenanceType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public record MaintenanceReminderResponse(

        UUID id,
        String title,
        LocalDate dueDate,
        String workshop,
        MaintenanceType type,
        LocalDate appointmentDate,
        LocalTime appointmentTime,
        LocalDate notificationDate,
        LocalDateTime notificationSentAt,
        Integer odometer,
        BigDecimal estimatedCost,
        String notes,

        UUID userId,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,

        UUID carId,
        String carNickname,
        String carBrandName,
        String carModelName,
        String plateNumber

) {
}

