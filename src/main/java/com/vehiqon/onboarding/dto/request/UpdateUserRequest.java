package com.vehiqon.onboarding.dto.request;

import jakarta.validation.constraints.Email;
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
public class UpdateUserRequest {
    @Size(max= 50)
    private String firstName;

    @Size(max= 50)
    private String lastName;

    private String address;

    private String gender;

    @Email(message= "Invalid email address")
    private String email;

    @Pattern(
            regexp = "\\+?[0-9]{10,15}",
            message="Invalid phone number"
    )
    private String phoneNumber;

    private String alternativePhoneNumber;

//    @NotBlank
//    private String password;

}
