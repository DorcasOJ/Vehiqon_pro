package com.vehiqon.features.carmgmt.dto;


import com.vehiqon.features.carmgmt.enums.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
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
            LocalDate licenseExpiry,
            CarStatus status,
            CarBrandDto.CarBrandResponse carBrand,
            CarModelDto.CarModelResponse carModel
    ){}


    public record CarEntityResponse(
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
            LocalDate licenseExpiry,
            CarStatus status,
            UUID carBrandId,
            UUID carModelId
    ){}


    public record CreateCarRequest (
//            @NotNull(message = "UserId cannot be null") UUID userId,
            @NotNull(message = "Brand cannot be null") UUID carBrandId,
            @NotNull(message = "Model cannot be null") UUID carModelId,
            String nickname,
            @Schema(example = "123456")
            @NotBlank(message = "VIN is required") String vin,

            @Schema(example = "GH3456")
            @NotBlank(message = "PlateNumber is required") String plateNumber,

            @NotNull(message = "Fuel type cannot be null") FuelType fuelType,

            @Schema(example = "black")
            String color,

            @Schema(example = "658JH456")
            String engineNumber,

            @Schema(example = "AUTOMATIC")
            @NotNull(message = "Transmission type cannot be null")
            TransmissionEnum transmission,
            @Schema(example = "300")
            @PositiveOrZero Long odometer,

            @Schema(example = "2000")
            @Min(1900) @Max(2100) Integer year,

            @Schema(example = "25-05-2025")
            String purchaseDate,
            @Schema(example = "25-05-2030")
            String licenseExpiry,
            @Schema(example = "ACTIVE")
            CarStatus status
    )  implements CarRequest {
    }

    public record UpdateCarRequest  (

        String nickname,
        String plateNumber,
        String color,
        String vin,
        Integer year,
        String engineNumber,
        FuelType fuelType,
        TransmissionEnum transmission,
         Long odometer,
         String purchaseDate,
         String licenseExpiry,
         CarStatus status,
        UUID carBrandId,
        UUID carModelId
    )  implements CarRequest {}

    public interface CarRequest {
        String vin();
        String plateNumber();
        String engineNumber();
        UUID carBrandId();
        UUID carModelId();

    }

    public record CarStatisticsResponse(
            long totalCars,
            long activeCars,
            long deletedCars
    ){}
}
