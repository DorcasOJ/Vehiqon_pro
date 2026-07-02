package com.vehiqon.common.entity;

import com.vehiqon.carmgmt.entities.Brand;
import com.vehiqon.common.enums.CarStatus;
import com.vehiqon.common.enums.FuelType;
import com.vehiqon.common.enums.TransmissionEnum;
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
public class Car extends BaseEntity{

    private String nickname;
    @Column(unique = true, nullable = false)
    private String vin;

    @Column(unique = true, nullable = false)
    private String plateNumber;

    private String color;

    private Integer year;

    private String engineNumber;

    @Enumerated(EnumType.STRING)
    private FuelType fuelType;

    @Enumerated(EnumType.STRING)
    private TransmissionEnum transmission;

    private Long odometer;

    private LocalDate purchaseDate;

    private LocalDate insuranceExpiry;

    private LocalDate licenseExpiry;

    @Enumerated(EnumType.STRING)
    private CarStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id", nullable = false)
    private Model model;

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<ServiceHistory> serviceHistory = new HashSet<>();

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<MaintenanceReminder> maintenanceReminders = new HashSet<>();

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Documents> documents = new HashSet<>();
}
