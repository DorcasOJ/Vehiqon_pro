package com.vehiqon.features.carmgmt.dto.response;

import com.vehiqon.features.carmgmt.enums.FuelType;
import com.vehiqon.features.carmgmt.enums.TransmissionEnum;

import java.util.UUID;

public record CarResponse(
        UUID id,
        String brand,
        String model,
        String vin,
        String plateNumber,
        Integer year,
        String color,
        Integer mileage,
        String engineNumber,
        FuelType fuelType,
        TransmissionEnum transmission
) {
}
