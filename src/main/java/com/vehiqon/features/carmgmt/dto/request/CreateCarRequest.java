package com.vehiqon.features.carmgmt.dto.request;

import com.vehiqon.features.carmgmt.enums.FuelType;
import com.vehiqon.features.carmgmt.enums.TransmissionEnum;
import jakarta.validation.constraints.*;

import java.util.UUID;

public record CreateCarRequest (

        @NotNull(message = "UserId cannot be null") UUID userId,
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
    @PositiveOrZero Long mileage,
    @Min(1900) @Max(2100) Integer year
    ){
}
