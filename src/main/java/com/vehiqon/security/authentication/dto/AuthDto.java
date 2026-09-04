package com.vehiqon.security.authentication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class AuthDto {
    private AuthDto(){}

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

    public record LogoutRequest(
            @NotBlank String refreshToken
    ) {
    }

    public record RefreshTokenRequest(
            @NotBlank String refreshToken
    ) {

    }

    public record ResendVerificationRequest(
            @Email @NotBlank String email
    ) {
    }

    public record ChangePasswordRequest(
            @NotBlank String currentPassword,
            @NotBlank String newPassword,
            @NotBlank String confirmPassword
    ){}

    public record ForgotPasswordRequest(
            @NotBlank @Email String email
    ){}

    public record ResetPasswordRequest(
            @NotBlank String token,
            @NotBlank String newPassword,
            @NotBlank String confirmPassword
    ){}

}
