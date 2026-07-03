package com.vehiqon.features.carmgmt.dto.response;

import java.util.UUID;

public record CarModelResponse(
        UUID id,
        String name
) {
}
