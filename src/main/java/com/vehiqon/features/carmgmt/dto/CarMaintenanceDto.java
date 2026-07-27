package com.vehiqon.features.carmgmt.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vehiqon.features.carmgmt.enums.MaintenanceStatus;
import com.vehiqon.features.carmgmt.enums.MaintenanceType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public class CarMaintenanceDto {
    private CarMaintenanceDto() {}


//    @Enumerated(EnumType.STRING)
//    private MaintenanceType maintenanceType;
//    @Enumerated(EnumType.STRING)
//    private MaintenanceStatus maintenanceStatus;
//
//    @Column(nullable = false)
//    private LocalDate appointment_date;
//    @Column(nullable = false)
//    private LocalTime appointment_time;
//
//    private Integer odometer;
//    private BigDecimal estimated_cost;
//
//    private String workshop;
//    private String note;
    public record CreateCarMaintenanceRequest(
            @NotNull(message = "Car is required")
            UUID carId,

            @NotNull(message = "User is required")
            UUID userId,

            @NotBlank(message = "Title is required")
            String title,
            String description,
            MaintenanceType maintenanceType,
            MaintenanceStatus maintenanceStatus,
            @FutureOrPresent(message = "Appointment date cannot be in the past")
            @JsonFormat(pattern = "dd-MM-yyyy")
//            @DateTimeFormat(pattern = "dd-MM-yyyy")
            LocalDate appointmentDate,
            @JsonFormat(pattern = "HH:mm")
            LocalTime appointmentTime,
            BigDecimal estimatedCost,
            String workshop,
            String notes

    ) {}


    public record UpdateCarMaintenanceRequest(
            UUID carId,
            UUID userId,String title,
            String description,
            MaintenanceType maintenanceType,
            MaintenanceStatus maintenanceStatus, LocalDate appointmentDate,
             LocalTime appointmentTime,
            BigDecimal estimatedCost,
            String workshop,
            String notes
    ) {}
}
