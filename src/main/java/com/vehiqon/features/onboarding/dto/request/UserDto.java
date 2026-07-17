package com.vehiqon.features.onboarding.dto.request;

import com.vehiqon.common.enums.RoleEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Set;

public class UserDto {
    private UserDto(){}

    public record CreateUserRequest (
        @NotBlank(message = "First name is required")
        @Size(max= 50) String firstName,

        @NotBlank(message = "Last name is required")
        @Size(max= 50) String lastName,
         String bvn, String address,
        @NotBlank String gender,
        @NotBlank @Email(message= "Invalid email address") String email,
        @NotBlank
        @Pattern(
                regexp = "\\+?[0-9]{10,15}",
                message="Invalid phone number"
        ) String phoneNumber,
        @NotBlank  String password,
        RoleEnum role
){
        public CreateUserRequest {
            if(role == null) {
                role = RoleEnum.ROLE_USER;
            }
        }
    }

    public record UpdateRolesRequest(
           Set<RoleEnum> add,
           Set<RoleEnum> remove

    ){}

    public record SyncRolesRequest(
            Set<RoleEnum> roles
    ){}


    public record UpdateUserRequest (
         String firstName,
         String lastName,
         String address,
         String gender,
         String email,
         String phoneNumber,
         String bvn
        //    @NotBlank
        //    private String password
    ){ }

}
