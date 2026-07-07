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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "brand_id")
    private BrandEntity brand;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "model_id")
    private CarModelEntity model;

    @Builder.Default
    @OneToMany(mappedBy = "carEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ServiceHistory> serviceHistory = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "carEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MaintenanceReminderEntity> maintenanceReminders = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "carEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Documents> documents = new HashSet<>();
}
