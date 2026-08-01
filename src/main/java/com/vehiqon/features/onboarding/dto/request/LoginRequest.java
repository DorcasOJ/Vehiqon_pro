package com.vehiqon.features.onboarding.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @Schema(example = "john@email.com",
                description = "User email"
        )
        @Email @NotBlank String email,
        @Schema(
                description = "User password",
                example = "password"
        )
                           @NotBlank String password
) {
}
