package com.vehiqon.features.carmgmt.dto.request;

import com.vehiqon.common.enums.CarStatus;
import com.vehiqon.common.enums.FuelType;
import com.vehiqon.common.enums.TransmissionEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class UpdateCarRequest {

    @NotBlank
    private String nickname;

    @NotBlank
    private String plateNumber;

    private String color;

    @NotNull
    private Integer year;

    private String engineNumber;

    @NotNull
    private FuelType fuelType;

    @NotNull
    private TransmissionEnum transmission;

    private Long odometer;

    private LocalDate purchaseDate;

    private LocalDate insuranceExpiry;

    private LocalDate licenseExpiry;

    private CarStatus status;

    @NotNull
    private UUID brandId;

    @NotNull
    private UUID modelId;

    // getters/setters
}