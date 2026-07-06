package com.vehiqon.features.carmgmt.dto;


import com.vehiqon.features.carmgmt.entities.BrandEntity;
import com.vehiqon.features.carmgmt.entities.CarModelEntity;
import com.vehiqon.features.carmgmt.enums.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class CarDto {

    private CarDto(){}

    public record CarResponse(
            UUID id,
            String nickname,
            String vin,
            String plateNumber,
            String color,
            Integer year,
            String engineNumber,
            FuelType fuelType,
            TransmissionEnum transmission,
            Long odometer,
            LocalDate purchaseDate,
//            LocalDate insuranceExpiry,
            LocalDate licenseExpiry,
            CarStatus status,
            CarBrandDto.CarBrandResponse brand,
            CarModelDto.CarModelResponse model
    ){}


    public record CreateCarRequest (
//            @NotNull(message = "UserId cannot be null") UUID userId,
            @NotNull(message = "Brand cannot be null") UUID brandId,
            @NotNull(message = "Model cannot be null") UUID modelId,
            String nickname,
            @NotBlank(message = "VIN is required") String vin,
            @NotBlank(message = "PlateNumber is required") String plateNumber,
            @NotNull(message = "Fuel type cannot be null") FuelType fuelType,
            String color,

            String engineNumber,
            @NotNull(message = "Transmission type cannot be null")
            TransmissionEnum transmission,
            @PositiveOrZero Long odometer,
            @Min(1900) @Max(2100) Integer year,

            String purchaseDate,
            String licenseExpiry
    ){
    }

    public record UpdateCarRequest (

        @NotBlank String nickname,

        @NotBlank String plateNumber,

        String color,

        @NotNull Integer year,
        String engineNumber,

        @NotNull FuelType fuelType,

        @NotNull TransmissionEnum transmission,

         Long odometer,

         LocalDate purchaseDate,

//         LocalDate insuranceExpiry,

         LocalDate licenseExpiry,

         CarStatus status,

        @NotNull UUID brandId,

        @NotNull UUID modelId

    ){}
}
