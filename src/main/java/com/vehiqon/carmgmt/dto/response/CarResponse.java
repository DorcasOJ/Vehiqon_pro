package com.vehiqon.carmgmt.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarResponse {
    private UUID id;
    private String nickname;
    private String vin;
    private String plateNumber;
    private String brand;
    private String model;
    private String fuelType;
    private String transmission;
}
