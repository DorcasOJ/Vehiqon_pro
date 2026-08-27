package com.vehiqon.features.carmgmt.dto.response;

import com.vehiqon.features.carmgmt.enums.MaintenanceType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record MaintenanceReminderResponse(

        UUID reminderId,
        String email,
        String firstName,
        String lastName,
        String phoneNumber,
        LocalDate appointmentDate,
        LocalTime appointmentTime,
        BigDecimal estimatedCost,
        UUID maintenanceId,
        String title,
        String workshop,
        UUID carId,
        String carBrandName,
        String carModelName,
        String carNickname,
        MaintenanceType type

) {
}

