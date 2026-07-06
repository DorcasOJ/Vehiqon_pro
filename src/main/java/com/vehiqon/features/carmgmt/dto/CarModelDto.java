package com.vehiqon.features.carmgmt.dto;

import java.util.UUID;

public class CarModelDto {

    private CarModelDto(){}

    public record CarModelResponse(
            UUID id,
            String name
    ) {
    }

}
