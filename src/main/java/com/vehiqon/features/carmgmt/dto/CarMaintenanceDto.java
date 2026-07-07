package com.vehiqon.features.carmgmt.dto;

import com.vehiqon.features.carmgmt.enums.MaintenanceType;
import com.vehiqon.features.carmgmt.enums.MaintenanceStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public class CarMaintenanceDto {

    private CarMaintenanceDto() {}

    public record CreateMaintenanceRequest(
            @NotBlank(message = "Car is required")
            UUID carId,

            @NotBlank(message = "Title is required")
            String title,
            String description,
            MaintenanceType type,
            @FutureOrPresent(message = "Appointment date cannot be in the past")
            String appointmentDate,
            String appointmentTime,
            @FutureOrPresent(message = "Due date cannot be in the past")
            String dueDate,
            @FutureOrPresent(message = "Notification date cannot be in the past")
            String notificationDate,
            @PositiveOrZero(message = "Odometer cannot be negative")
            Integer odometer,
            @PositiveOrZero(message = "Estimated cost cannot be negative")
            BigDecimal estimatedCost,
            String workshop,
            String notes

    ) {}

    public record MaintenanceResponse(

            UUID id,
            UUID carId,
            String carNickname,
            String brand,
            String model,
            String title,
            MaintenanceType type,
            MaintenanceStatus status,
            LocalDate appointmentDate,
            LocalTime appointmentTime,
            LocalDate notificationDate,
            LocalDate dueDate,
            Integer odometer,
            BigDecimal estimatedCost,
            String workshop,

            String notes

    ) {}

    public record UpdateMaintenanceRequest(
            String title,
            String description,
            MaintenanceType type,
            MaintenanceStatus status,
            LocalDate appointmentDate,
            LocalTime appointmentTime,
            LocalDate notificationDate,
            LocalDate dueDate,
           Integer odometer,
            BigDecimal estimatedCost,
            String workshop,
            String notes

    ) {}
}
