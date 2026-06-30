package com.casdore.car_mgmt.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    private String otherName;
    private String address;
    @NotBlank
    private String gender;
    @NotBlank
    private String email;
    @NotBlank
    private String phoneNumber;

    private String alternativePhoneNumber;
    @NotBlank
    private String password;

}
