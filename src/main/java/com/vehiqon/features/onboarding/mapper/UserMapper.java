package com.vehiqon.features.onboarding.mapper;

import com.vehiqon.common.entity.User;
import com.vehiqon.features.onboarding.dto.CreateUserRequest;
import com.vehiqon.features.onboarding.dto.request.UpdateUserRequest;
import com.vehiqon.features.onboarding.dto.response.UserProfileResponse;
import com.vehiqon.features.onboarding.dto.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

//    return from creating user
    UserResponse toResponse(User user);

//    return user profile
    @Mapping(target = "fullName",
    expression = """
            java(
                  java.util.stream.Stream
                      .of(user.getFirstName(),
                          user.getLastName())
                      .filter(java.util.Objects::nonNull)
                      .filter(s -> !s.isBlank())
                      .collect(java.util.stream.Collectors.joining(" "))
              )
            """)
    UserProfileResponse toProfile(User user);

//    get list of all user
    List<UserResponse> toResponses(List<User> users);

//    create new user
@Mapping(target = "id", ignore = true)
@Mapping(target = "createdAt", ignore = true)
@Mapping(target = "updatedAt", ignore = true)
@Mapping(target = "primaryAccountNumber", ignore = true)
@Mapping(target = "roles", ignore = true)
@Mapping(target = "status", ignore = true)
@Mapping(target = "isVerified", ignore = true)
@Mapping(target = "cars", ignore = true)
@Mapping(target = "notifications", ignore = true)
@Mapping(target = "auditLog", ignore = true)
@Mapping(target = "userPlan", ignore = true)
@Mapping(target = "password", ignore = true)
     User toEntity(CreateUserRequest request);

//    update existing user
@Mapping(target = "id", ignore = true)
@Mapping(target = "createdAt", ignore = true)
@Mapping(target = "updatedAt", ignore = true)
@Mapping(target = "primaryAccountNumber", ignore = true)
@Mapping(target = "status", ignore = true)
@Mapping(target = "isVerified", ignore = true)
@Mapping(target = "roles", ignore = true)
@Mapping(target = "cars", ignore = true)
@Mapping(target = "notifications", ignore = true)
@Mapping(target = "auditLog", ignore = true)
@Mapping(target = "userPlan", ignore = true)
@Mapping(target = "password", ignore = true)
    void UpdateUser(UpdateUserRequest request, @MappingTarget User user);
}
