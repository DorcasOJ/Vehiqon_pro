package com.vehiqon.features.onboarding.dto;

import com.vehiqon.security.authorization.enums.RoleEnum;
import com.vehiqon.common.exception.BadRequestException;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

public class UserDto {
    private UserDto(){}

    public record CreateUserRequest (
            @Schema(example = "john",
                    description = "User first name"
            )
            @NotBlank(message = "First name is required")
            @Size(max= 50) String firstName,

            @Schema(example = "doe",
                    description = "User last name"
            )
            @NotBlank(message = "Last name is required")
            @Size(max= 50) String lastName,

            @Schema(example = "123, Some address, LG, NG",
                    description = "User address"
            )
             String bvn, String address,
            @NotBlank String gender,

            @Schema(example = "john@email.com",
                    description = "User email"
            )
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

    ){
        public UpdateRolesRequest {
            add = (add == null) ? Set.of() : Set.copyOf(add);
            remove = (remove == null) ? Set.of() : Set.copyOf(remove);

            if(add.isEmpty() && remove.isEmpty()) {
                throw new BadRequestException("At least one role must be added or removed.");
            }

            if(!Collections.disjoint(add, remove)) {
                throw new BadRequestException("A role cannot be added and removed in the same request.");
            }
        }

        private static void validateRolePrefix(Set<RoleEnum> roles) {
            for(RoleEnum role : roles) {
                if(role == null || !role.name().startsWith("ROLE_")) {
                    throw new BadRequestException("All roles must start with 'ROLE_'. Found invalid role: " + role);
                }
            }
        }
    }


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

    public record UserResponse(
            UUID id,
            String firstName,
            String lastName,
            String email,
            String phoneNumber,
            Set<RoleEnum> roles,
            String status,
            String gender
//            String bvn
    ){}

}
