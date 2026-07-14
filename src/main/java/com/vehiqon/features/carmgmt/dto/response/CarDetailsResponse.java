package com.vehiqon.features.carmgmt.dto.response;


import com.vehiqon.features.carmgmt.enums.CarStatus;
import com.vehiqon.features.carmgmt.enums.FuelType;
import com.vehiqon.features.carmgmt.enums.TransmissionEnum;

import java.time.LocalDate;
import java.util.UUID;

public record CarDetailsResponse(
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
        String brandName,
        UUID carModelId,
        String modelName
){}

