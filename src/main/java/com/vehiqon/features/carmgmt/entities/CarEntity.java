package com.vehiqon.features.carmgmt.entities;

import com.vehiqon.common.entity.*;

import com.vehiqon.features.carmgmt.enums.*;
import com.vehiqon.features.onboarding.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name="cars")
public class CarEntity extends BaseEntity {

    private String nickname;

    @Column(unique = true, nullable = false)
    private String vin;

    @Column(unique = true, nullable = false)
    private String plateNumber;

    private String color;

    private Integer year;

    @Column(unique = true)
    private String engineNumber;

    @Enumerated(EnumType.STRING)
    private FuelType fuelType;

    @Enumerated(EnumType.STRING)
    private TransmissionEnum transmission;

    private Long odometer;

    private LocalDate purchaseDate;

    private LocalDate licenseExpiry;

    @Enumerated(EnumType.STRING)
    private CarStatus status;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "car_brand_id", nullable = false)
    private UUID carBrandId;

    @Column(name = "car_model_id", nullable = false)
    private UUID carModelId;


}
