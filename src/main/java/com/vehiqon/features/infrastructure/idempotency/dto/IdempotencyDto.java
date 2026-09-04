package com.vehiqon.features.infrastructure.idempotency.dto;

import java.io.Serializable;

public record IdempotencyDto(
        String status, // "PROCESSING" or "COMPLETED"
        int statusCode, // e.g., 200, 201
        Object responseBody // The serialized JSON payload returned to client
) implements Serializable {
    public static IdempotencyDto processing() {
        return new IdempotencyDto("PROCESSING", 0, null);
    }

    public static IdempotencyDto completed(int statusCode, Object responseBody) {
        return new IdempotencyDto("COMPLETED", statusCode, responseBody);
    }
}
