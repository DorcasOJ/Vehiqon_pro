package com.vehiqon.features.carmgmt.dto;

import java.util.UUID;

public class CarBrandDto {

    private CarBrandDto(){}

    public record CarBrandResponse(
            UUID id,
            String name
    ) {
    }

}
