package com.vehiqon.features.carmgmt.dto.response;

import java.util.UUID;

public record CarBrandResponse(
        UUID id,
        String name
) {
}
