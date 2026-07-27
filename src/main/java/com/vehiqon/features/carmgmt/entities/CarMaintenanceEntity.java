package com.vehiqon.features.carmgmt.entities;

import com.vehiqon.common.entity.BaseEntity;
import com.vehiqon.features.carmgmt.enums.*;
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
@Table(name="car_maintenance")
public class CarMaintenanceEntity extends BaseEntity {

    @Column(name = "car_id", nullable = false)
    private UUID carId;
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private MaintenanceType maintenanceType;
    @Enumerated(EnumType.STRING)
    private MaintenanceStatus maintenanceStatus;

    @Column(nullable = false)
    private LocalDate appointmentDate;
    @Column(nullable = false)
    private LocalTime appointmentTime;

    private Integer odometer;
    private BigDecimal estimatedCost;

    private String workshop;
    private String note;

}
