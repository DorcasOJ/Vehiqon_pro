package com.casdore.car_mgmt.car.dto.request;

import com.casdore.car_mgmt.common.enums.FuelType;
import com.casdore.car_mgmt.common.enums.TransmissionEnum;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCarRequest {

    @NotBlank(message = "Nickname is required")
    private String nickname;

    @NotBlank(message = "VIN is required")
    private String vin;

    @NotBlank(message = "PlateNumber is required")
    private String plateNumber;

    @NotNull(message = "Brand cannot be null")
    private UUID brandId;

    @NotNull(message = "Model cannot be null")
    private UUID modelId;

    @NotNull(message = "Fuel type cannot be null")
    private FuelType fuelType;

    @NotNull(message = "Transmission type cannot be null")
    private TransmissionEnum transmission;

    @PositiveOrZero
    private Long odometer;

    @Min(1900)
    @Max(2100)
    private Integer year;


}
