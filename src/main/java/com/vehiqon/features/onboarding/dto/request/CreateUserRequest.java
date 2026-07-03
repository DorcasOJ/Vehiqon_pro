package com.vehiqon.features.onboarding.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserRequest {
    @NotBlank(message = "First name is required")
    @Size(max= 50)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max= 50)
    private String lastName;

    private String address;

    @NotBlank
    private String gender;

    @NotBlank
    @Email(message= "Invalid email address")
    private String email;

    @NotBlank
    @Pattern(
            regexp = "\\+?[0-9]{10,15}",
            message="Invalid phone number"
    )
    private String phoneNumber;

    @NotBlank
    private String password;

}
