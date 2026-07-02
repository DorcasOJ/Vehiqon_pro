package com.vehiqon.features.carmgmt.dto;


import com.vehiqon.common.enums.CarStatus;
import com.vehiqon.common.enums.FuelType;
import com.vehiqon.common.enums.TransmissionEnum;
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
            List<CarResponseData> data
    ){}

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CarResponseData {

        private UUID id;

        private String nickname;
        private String vin;
        private String plateNumber;
        private String color;
        private Integer year;
        private String engineNumber;

        private FuelType fuelType;
        private TransmissionEnum transmission;
        private Long odometer;

        private LocalDate purchaseDate;
        private LocalDate insuranceExpiry;
        private LocalDate licenseExpiry;

        private CarStatus status;

        private UUID userId;

        private UUID brandId;
        private String brandName;

        private UUID modelId;
        private String modelName;
    }

}
